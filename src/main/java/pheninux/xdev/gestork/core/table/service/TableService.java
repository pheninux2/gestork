package pheninux.xdev.gestork.core.table.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.customer.model.CustomerRole;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;
import pheninux.xdev.gestork.core.table.model.CustomerTable;
import pheninux.xdev.gestork.core.table.model.TableStatus;
import pheninux.xdev.gestork.core.table.repository.TableRepository;

import static pheninux.xdev.gestork.utils.Utils.getLogin;
import static pheninux.xdev.gestork.utils.Utils.renderAlert;

@Service
public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);
    private final TableRepository tableRepository;
    private final CustomerRepository customerRepository;
    private final AccessCodeService accessCodeService;
    private final BCryptPasswordEncoder passwordEncoder;

    public TableService(TableRepository tableRepository, CustomerRepository customerRepository, AccessCodeService accessCodeService, BCryptPasswordEncoder passwordEncoder, EmployeeRepository employeeRepository) {
        this.tableRepository = tableRepository;
        this.customerRepository = customerRepository;
        this.accessCodeService = accessCodeService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> generateCode(String clientLogin, int tableNumber) {

        try {
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
                code = accessCodeService.generateAndSaveAccessCode(customer,tableNumber);
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

        } catch (
                DataAccessException e) {
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
