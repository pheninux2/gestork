package pheninux.xdev.gestork.web.employee.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pheninux.xdev.gestork.core.dish.model.DishStatus;
import pheninux.xdev.gestork.core.dish.service.DishService;
import pheninux.xdev.gestork.web.dish.ApiDishController;

@RestController
@RequestMapping("/api/admin/dish")
public class AdminDishController {

    private static final Logger log = LoggerFactory.getLogger(AdminDishController.class);

    private final ApiDishController apiDishController;

    public AdminDishController(ApiDishController apiDishController, DishService dishService) {
        this.apiDishController = apiDishController;

    }

    @PostMapping("/add")
    public ResponseEntity<String> addDish(@RequestParam("name") String name,
                                          @RequestParam("description") String description,
                                          @RequestParam("price") Double price,
                                          @RequestParam("image") MultipartFile imageFile,
                                          @RequestParam("category") String category,
                                          @RequestParam("dishStatus") DishStatus dishStatus) {
        return apiDishController.addDish(name, description, price, imageFile, category, dishStatus);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Void> updateDish(@PathVariable Long id,
                                           @RequestParam(value = "newPrice", defaultValue = "0.0") double newPrice,
                                           @RequestParam(value = "specialPrice", defaultValue = "false") boolean specialPrice) {
        return apiDishController.updateDish(id, newPrice, specialPrice);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        return apiDishController.deleteDish(id);
    }
}