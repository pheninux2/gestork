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
    public String generateAndSaveAccessCode(Client client,int tableNumber) {

        String code = generateAccessCode(tableNumber);
        Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusMinutes(6)); // Code valide 1 heure
        AccessCode accessCode = new AccessCode();
        accessCode.setClient(client);
        accessCode.setCode(code);
        accessCode.setExpiryDate(expiryDate);
        accessCode.setUsed(false);

        try {
            accessCodeRepository.saveAndFlush(accessCode);
            return code;
        } catch (Exception e) {
            throw new RuntimeException("Error to save generating access code", e);
        }


    }

    public String generateAccessCode(int tableNumber) {
        // Générer un UUID
        UUID uuid = UUID.randomUUID();

        // Convertir l'UUID en chaîne et retirer les tirets
        String uuidString = uuid.toString().replaceAll("-", "");

        // Créer un StringBuilder pour le code d'accès
        StringBuilder accessCode = new StringBuilder();

        // Ajouter des caractères aléatoires (lettres et chiffres)
        for (int i = 0; i < 6; i++) {
            // Choisir un caractère aléatoire de l'UUID
            char randomChar = uuidString.charAt((int) (Math.random() * uuidString.length()));
            accessCode.append(randomChar);
        }

        // Ajouter le numéro de table
        accessCode.append("-");
        accessCode.append(tableNumber);

        return accessCode.toString(); // Retourne le code d'accès
    }

    public boolean isAccessCodeValid(String code) {
        AccessCode accessCode = accessCodeRepository.findByCode(code);
        if (accessCode == null || accessCode.isUsed() || accessCode.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        return true;
    }
}
