package pheninux.xdev.gestork.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class LoginCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique pour chaque enregistrement

    private String clientId; // Identifiant unique généré pour chaque client
    private String accessCode; // Code d'accès généré par le serveur
    private boolean isValid; // Indique si le ClientId et le AccessCode sont valides

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate; // Date et heure d'expiration de la session

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate; // Date et heure de création du code d'accès

    public LoginCustomer() {
        this.createdDate = new Date();
    }
}
