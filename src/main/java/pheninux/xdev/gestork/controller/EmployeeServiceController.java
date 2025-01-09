package pheninux.xdev.gestork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.model.ClientRole;
import pheninux.xdev.gestork.repository.ClientRepository;
import pheninux.xdev.gestork.service.AccessCodeService;

@RestController
@RequestMapping("/employee/service")
public class EmployeeServiceController {

    private final AccessCodeService accessCodeService;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeServiceController(AccessCodeService accessCodeService, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accessCodeService = accessCodeService;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('SERVER')")
    @PostMapping(value = "/generateCodeAccessWithLogin")
    public ResponseEntity<String> generateCodeAccessWithClientLogin(@RequestParam("login") String login) {

        if (login == null || login.isEmpty()) {
            String errorResponse = "<div class=\"alert alert-danger\">Le login est indispensable.</div>";
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Client client = clientRepository.findClientByLogin(login);
        if (client == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accessCodeService.generateAndSaveAccessCode(client);

        return ResponseEntity.ok(login);
    }

    @PreAuthorize("hasRole('SERVER')")
    @PostMapping(value = "/generateCodeAccessWithoutLogin")
    public ResponseEntity<String> generateCodeAccessWithoutClientLogin(@RequestParam("login") String login) {

        if (login == null || login.isEmpty()) {
            String errorResponse = "<div class=\"alert alert-danger\">Le login est indispensable.</div>";
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Client client = new Client();
        client.setLogin(login);
        client.setRole(ClientRole.CLIENT);
        client.setPassword(passwordEncoder.encode("123456"));
        clientRepository.saveAndFlush(client);
        accessCodeService.generateAndSaveAccessCode(client);

        String successResponse = "<div class=\"alert alert-success\">Client créé avec succès pour le login: " + login + ".</div>";
        return ResponseEntity.ok(successResponse);
    }

}
