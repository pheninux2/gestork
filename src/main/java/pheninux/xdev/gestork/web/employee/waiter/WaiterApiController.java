package pheninux.xdev.gestork.web.employee.waiter;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pheninux.xdev.gestork.core.accessCode.service.AccessCodeService;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;
import pheninux.xdev.gestork.core.table.repository.TableRepository;
import pheninux.xdev.gestork.core.table.service.TableService;

import static pheninux.xdev.gestork.utils.Utils.*;

@RestController
@RequestMapping(value = "/api/waiter")
public class WaiterApiController {

    private static final Logger log = LoggerFactory.getLogger(WaiterApiController.class);

    private final TableService tableService;

    public WaiterApiController(CustomerRepository customerRepository, TableRepository tableRepository, TableService tableService, AccessCodeService accessCodeService, BCryptPasswordEncoder passwordEncoder) {
        this.tableService = tableService;
    }

    @PostMapping(value = "/generateCode")
    public ResponseEntity<String> generateCode(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber, HttpServletResponse response) {
        if (!isAdmin() && !isWaiter()) {
            String alert = renderAlert("alert-danger", "Vous n'êtes pas autorisé à accéder à cette ressource.");
            return new ResponseEntity<>(alert, HttpStatus.OK);
        }
        return tableService.generateCode(clientLogin, tableNumber);
    }
}
