package pheninux.xdev.gestork.web.employee.admin.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import pheninux.xdev.gestork.core.dish.model.Category;
import pheninux.xdev.gestork.core.dish.model.Dish;
import pheninux.xdev.gestork.core.dish.model.DishStatus;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.utils.Utils;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;

import static pheninux.xdev.gestork.utils.Utils.renderAlertSingle;

@Controller
@RequestMapping("/api/admin/dish")
public class DishApiController {

    private static final Logger log = LoggerFactory.getLogger(DishApiController.class);
    private final DishService dishService;
    private final TemplateEngine templateEngine;

    public DishApiController(DishService dishService, TemplateEngine templateEngine) {
        this.dishService = dishService;
        this.templateEngine = templateEngine;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addDish(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("category") String category,
            @RequestParam("dishStatus") DishStatus dishStatus) {

        if (!Utils.isAdmin()) {
            String alert = renderAlertSingle("alert-danger", "Vous n'êtes pas autorisé à effectuer cette action.");
            return new ResponseEntity<>(alert, HttpStatus.FORBIDDEN);
        }

        if (imageFile == null || imageFile.isEmpty()) {
            String alert = renderAlertSingle("alert-warning-single", "L'image est requise.");
            return new ResponseEntity<>(alert, HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);

            Dish dish = new Dish();
            dish.setName(name);
            dish.setDescription(description);
            dish.setPrice(price);
            dish.setImage(base64Image);
            dish.setCategory(Category.valueOf(category));
            dish.setStatus(dishStatus);

            dishService.save(dish);
        } catch (IOException e) {
            String alert = renderAlertSingle("alert-danger", "Une erreur est survenue lors du traitement de l'image.");
            return new ResponseEntity<>(alert, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String alert = renderAlertSingle("alert-success", "Le plat est ajouté avec succès.");
        return new ResponseEntity<>(alert, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Void> updateDish(
            @PathVariable Long id,
            @RequestParam(value = "newPrice", defaultValue = "0.0") double newPrice,
            @RequestParam(value = "specialPrice", defaultValue = "false") boolean specialPrice) {

        if (!Utils.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .location(URI.create("/error/403"))
                    .build();
        }

        try {
            Dish dish = dishService.findById(id);
            if (dish == null) {
                log.error("Dish not found with id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .location(URI.create("/error/404"))
                        .build();
            }

            dish.setPrice(newPrice);
            dish.setSpecialPrice(specialPrice);
            dishService.save(dish);

            log.debug("Dish updated successfully with id: {}", id);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/fragments/dish/list"))
                    .build();
        } catch (Exception e) {
            log.error("Error while updating dish price: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .location(URI.create("/error/500"))
                    .build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {

        if (!Utils.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .location(URI.create("/error/403"))
                    .build();
        }

        try {
            dishService.deleteById(id);
            log.debug("Dish deleted successfully with id: {}", id);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/fragments/dish/list"))
                    .build();
        } catch (Exception e) {
            log.error("Error while deleting dish: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .location(URI.create("/error/500"))
                    .build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dish>> findAll() {
        return ResponseEntity.ok(dishService.findAll());
    }
}
