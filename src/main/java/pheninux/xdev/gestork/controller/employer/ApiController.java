package pheninux.xdev.gestork.controller.employer;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.model.ClientRole;
import pheninux.xdev.gestork.repository.ClientRepository;
import pheninux.xdev.gestork.service.AccessCodeService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);


    private final AccessCodeService accessCodeService;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ApiController(AccessCodeService accessCodeService, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accessCodeService = accessCodeService;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('SERVER')")
    @PostMapping(value = "/generateCodeAccess")
    public String generateCodeAccess(@RequestParam("clientLogin") String clientLogin, @RequestParam("tableNumber") int tableNumber, HttpServletResponse response) {
        try {
            Client client = clientRepository.findClientByLogin(clientLogin);
            String code;

            if (client != null) {
                code = accessCodeService.generateAndSaveAccessCode(client, tableNumber);
                log.debug("<div class=\"alert alert-success\">Code créé avec succès pour le Client: " + clientLogin + ".</div>");

            } else {
                Client newClient = new Client();
                newClient.setLogin(clientLogin);
                newClient.setRole(ClientRole.CLIENT);
                newClient.setPassword(passwordEncoder.encode("123456"));
                clientRepository.saveAndFlush(newClient);
                code = accessCodeService.generateAndSaveAccessCode(newClient, tableNumber);
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

}
