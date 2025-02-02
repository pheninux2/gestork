package pheninux.xdev.gestork.web.employee.waiter;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static pheninux.xdev.gestork.utils.Utils.*;

@RestController
@RequestMapping(value = "/api/waiter")
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

    @PostMapping(value = "/generateCode")
    public ResponseEntity<String> generateCode(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber, HttpServletResponse response) {
        try {
            if (!isAdmin() && !isWaiter()) {
                String alert = renderAlert("alert-danger", "Vous n'êtes pas autorisé à accéder à cette ressource.");
                return new ResponseEntity<>(alert, HttpStatus.OK);
            }

            CustomerTable customerTable = tableRepository.findTableByNumber(tableNumber);

            if (!doesTableExist(tableNumber)) {
                String alert = renderAlert("alert-danger", "La table n'existe pas.");
                return new ResponseEntity<>(alert, HttpStatus.OK);
            }

            if (customerTable.getStatut() == TableStatus.BUSY) {
                String alert = renderAlert("alert-danger", "La table est déjà occupée.");
                return new ResponseEntity<>(alert, HttpStatus.OK);
            }

            Customer customer = customerRepository.findClientByLogin(clientLogin);
            String code;

            if (customer != null) {
                code = accessCodeService.generateAndSaveAccessCode(customer, tableNumber);
                tableRepository.updateTableStatus(TableStatus.BUSY, tableNumber);
                log.debug("Code créé avec succès pour le Client: " + clientLogin);

            } else {
                Customer newCustomer = new Customer();
                newCustomer.setLogin(clientLogin);
                newCustomer.setRole(CustomerRole.CUSTOMER);
                newCustomer.setPassword(passwordEncoder.encode("123456"));
                customerRepository.saveAndFlush(newCustomer);
                code = accessCodeService.generateAndSaveAccessCode(newCustomer, tableNumber);
                tableRepository.updateTableStatus(TableStatus.BUSY, tableNumber);
                log.debug("Client créé avec succès pour le login: " + clientLogin);
            }

            // Réponse HTML avec redirection
            String htmlResponse = "<script>window.location.href='/waiter/displayCode?code=" + code + "';</script>";
            return ResponseEntity.ok(htmlResponse);

        } catch (DataAccessException e) {
            log.error("Database error: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(renderAlert("alert-danger", "Erreur de base de données ! Veuillez réessayer."));
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(renderAlert("alert-danger", "Une erreur inattendue est survenue. Veuillez réessayer."));
        }

    }

    private boolean doesTableExist(int tableNumber) {
        return tableRepository.findTableByNumber(tableNumber) != null;
    }
}
