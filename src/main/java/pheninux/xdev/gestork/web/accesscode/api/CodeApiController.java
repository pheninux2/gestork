package pheninux.xdev.gestork.web.accesscode.api;

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
@RequestMapping(value = "/api/code")
public class CodeApiController {

    private static final Logger log = LoggerFactory.getLogger(CodeApiController.class);

    private final TableService tableService;
    private final AccessCodeService accessCodeService;

    public CodeApiController(CustomerRepository customerRepository, TableRepository tableRepository, TableService tableService, AccessCodeService accessCodeService, BCryptPasswordEncoder passwordEncoder, AccessCodeService accessCodeService1) {
        this.tableService = tableService;
        this.accessCodeService = accessCodeService1;
    }

    @PostMapping(value = "/generate")
    public ResponseEntity<String> generate(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber) {
        if (!isAdmin() && !isWaiter()) {
            String alert = renderAlertSingle("alert-danger", "Vous n'êtes pas autorisé à accéder à cette ressource.");
            return new ResponseEntity<>(alert, HttpStatus.OK);
        }
        return tableService.generateCode(clientLogin, tableNumber);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<String> checkCodeAccess(@RequestParam("code") String code) {
        if (accessCodeService.isAccessCodeValid(code)) {
            return ResponseEntity.status(200)
                    .body("<script>sessionStorage.setItem('code', '" + code + "'); " +
                            "window.location.href='/view/customer/menu/" + code + "';</script>");
        } else {
            String alert = renderAlertSingle("alert-danger", "Le code est invalide");
            return new ResponseEntity<>(alert, HttpStatus.OK);
        }
    }
}
