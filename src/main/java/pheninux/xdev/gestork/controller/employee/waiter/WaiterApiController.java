package pheninux.xdev.gestork.controller.employee.waiter;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pheninux.xdev.gestork.model.Customer;
import pheninux.xdev.gestork.model.CustomerRole;
import pheninux.xdev.gestork.model.CustomerTable;
import pheninux.xdev.gestork.model.TableStatus;
import pheninux.xdev.gestork.repository.CustomerRepository;
import pheninux.xdev.gestork.repository.TableRepository;
import pheninux.xdev.gestork.service.AccessCodeService;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;
import static pheninux.xdev.gestork.utils.Utils.isWaiter;

@RestController
@RequestMapping(value = "/api")
public class WaiterApiController {

    private static final Logger log = LoggerFactory.getLogger(WaiterApiController.class);

    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final AccessCodeService accessCodeService;
    private final BCryptPasswordEncoder passwordEncoder;

    public WaiterApiController(CustomerRepository customerRepository, TableRepository tableRepository, AccessCodeService accessCodeService, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
        this.accessCodeService = accessCodeService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/generateCodeAccess")
    public String generateCodeAccess(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber, HttpServletResponse response) {
        try {
            if (!isAdmin() && !isWaiter()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return "<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                        "<strong>Accès refusé !</strong> Vous n'êtes pas autorisé à accéder à cette ressource." +
                        "</div>";
            }

            CustomerTable customerTable = tableRepository.findTableByNumber(tableNumber);

            if (!doesTableExist(tableNumber)) {
                return "<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                        "<strong>Erreur !</strong> La table n'existe pas." +
                        "</div>";
            }

            if (customerTable.getStatut() == TableStatus.BUSY) {
                return "<div class=\"alert alert-danger\" style=\"margin-top: 20px; border: 1px solid #ff0000; background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">" +
                        "<strong>Erreur !</strong> La table est deja occupée." +
                        "</div>";
            }

            Customer customer = customerRepository.findClientByLogin(clientLogin);
            String code;

            if (customer != null) {
                code = accessCodeService.generateAndSaveAccessCode(customer, tableNumber);
                tableRepository.updateTableStatus(TableStatus.BUSY, tableNumber);
                log.debug("<div class=\"alert alert-success\">Code créé avec succès pour le Client: " + clientLogin + ".</div>");

            } else {
                Customer newCustomer = new Customer();
                newCustomer.setLogin(clientLogin);
                newCustomer.setRole(CustomerRole.CUSTOMER);
                newCustomer.setPassword(passwordEncoder.encode("123456"));
                customerRepository.saveAndFlush(newCustomer);
                code = accessCodeService.generateAndSaveAccessCode(newCustomer, tableNumber);
                tableRepository.updateTableStatus(TableStatus.BUSY, tableNumber);
                log.debug("<div class=\"alert alert-success\">Client créé avec succès pour le login: " + clientLogin + ".</div>");
            }

            return "<script>window.location.href='/waiter/showAccessCode-page?code=" + code + "';</script>";

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

    private boolean doesTableExist(int tableNumber) {
        return tableRepository.findTableByNumber(tableNumber) != null;
    }
}
