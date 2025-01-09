package pheninux.xdev.gestork.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.AccessCode;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.repository.AccessCodeRepository;
import pheninux.xdev.gestork.repository.ClientRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessCodeService {

    private final AccessCodeRepository accessCodeRepository;
    private final ClientRepository clientRepository;

    public AccessCodeService(AccessCodeRepository accessCodeRepository, ClientRepository clientRepository) {
        this.accessCodeRepository = accessCodeRepository;
        this.clientRepository = clientRepository;
    }

    @PreAuthorize("hasRole('SERVER')")
    public void generateAndSaveAccessCode(Client client) {

        String code = UUID.randomUUID().toString();
        Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusHours(1)); // Code valide 1 heure

        AccessCode accessCode = new AccessCode();
        accessCode.setClient(client);
        accessCode.setCode(code);
        accessCode.setExpiryDate(expiryDate);
        accessCode.setUsed(false);

        try {
            accessCodeRepository.save(accessCode);
        } catch (Exception e) {
            throw new RuntimeException("Error to save generating access code", e);
        }

    }

    public boolean isAccessCodeValid(String code) {
        AccessCode accessCode = accessCodeRepository.findByCode(code);
        if (accessCode == null || accessCode.isUsed() || accessCode.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        return true;
    }
}
