package pheninux.xdev.gestork.controller.employer;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pheninux.xdev.gestork.model.Category;
import pheninux.xdev.gestork.model.Customer;
import pheninux.xdev.gestork.model.CustomerRole;
import pheninux.xdev.gestork.model.Dish;
import pheninux.xdev.gestork.repository.CustomerRepository;
import pheninux.xdev.gestork.repository.DishRepository;
import pheninux.xdev.gestork.service.AccessCodeService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeApiController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeApiController.class);


    private final AccessCodeService accessCodeService;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DishRepository dishRepository;

    public EmployeeApiController(AccessCodeService accessCodeService, CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder, DishRepository dishRepository) {
        this.accessCodeService = accessCodeService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.dishRepository = dishRepository;
    }

    @PostMapping(value = "/generateCodeAccess")
    public String generateCodeAccess(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber, HttpServletResponse response) {
        try {
            Customer customer = customerRepository.findClientByLogin(clientLogin);
            String code;

            if (customer != null) {
                code = accessCodeService.generateAndSaveAccessCode(customer, tableNumber);
                log.debug("<div class=\"alert alert-success\">Code créé avec succès pour le Client: " + clientLogin + ".</div>");

            } else {
                Customer newCustomer = new Customer();
                newCustomer.setLogin(clientLogin);
                newCustomer.setRole(CustomerRole.CUSTOMER);
                newCustomer.setPassword(passwordEncoder.encode("123456"));
                customerRepository.saveAndFlush(newCustomer);
                code = accessCodeService.generateAndSaveAccessCode(newCustomer, tableNumber);
                log.debug("<div class=\"alert alert-success\">Client créé avec succès pour le login: " + clientLogin + ".</div>");
            }

            return "<script>window.location.href='/employee/showAccessCode-page?code=" + code + "';</script>";

        } catch (DataAccessException e) {
            log.error("Database error: " + e.getMessage());
            return "<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<strong>Erreur de base de données !</strong> Veuillez réessayer." +
                    "</div>";
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            return "<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<strong>Erreur !</strong> Une erreur inattendue est survenue. Veuillez réessayer." +
                    "</div>";
        }

    }


    @PostMapping(value = "/addDish")
    public ResponseEntity<String> addDish(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("category") String category) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // Vérification des permissions
        if (!isAdmin) {
            return new ResponseEntity<>("<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<strong>Erreur !</strong> Vous n'avez pas la permission d'accéder à cette page." +
                    "</div><script>setTimeout(function() { document.querySelector('.alert-danger').remove(); }, 5000);</script>", HttpStatus.FORBIDDEN);
        }

        // Vérification de l'image
        if (imageFile == null || imageFile.isEmpty()) {
            return new ResponseEntity<>("<div class=\"alert alert-warning\" style=\"margin-top: 20px; border: 1px solid #ffc107; background-color: #fff3cd; color: #856404; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<strong>Attention !</strong> L'image est requise." +
                    "</div><script>setTimeout(function() { document.querySelector('.alert-warning').remove(); }, 5000);</script>", HttpStatus.BAD_REQUEST);
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

            dishRepository.save(dish);
        } catch (IOException e) {
            return new ResponseEntity<>("<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                    "<strong>Erreur !</strong> Une erreur est survenue lors du traitement de l'image." +
                    "</div><script>setTimeout(function() { document.querySelector('.alert-danger').remove(); }, 5000);</script>", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("<div class=\"alert alert-success\" style=\"margin-top: 20px; border: 1px solid #28a745; background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                "<strong>Succès !</strong> Le plat est ajouté avec succès." +
                "</div><script>setTimeout(function() { document.querySelector('.alert-success').remove(); }, 5000);</script>", HttpStatus.OK);
    }


    @PostMapping("/getDishes")
    public ResponseEntity<List<Dish>> getDishes() {
        return new ResponseEntity<>(dishRepository.findAll(), HttpStatus.OK);
    }

}
