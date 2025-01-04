package pheninux.xdev.gestork.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId; // Identifiant unique, clé primaire
    @Getter
    private String login; // Identifiant du client
    private String password; // Identifiant du client
    private String email; // Adresse e-mail du client
    private String phoneNumber; // Numéro de téléphone du client
    private String firstName; // Prénom du client
    private String lastName; // Nom de famille du client
    private String address; // Adresse du client (si nécessaire)
    private boolean profileCompleted; // Indique si le client a complété son profil


    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate; // Date de création du profil du client

    @ElementCollection(targetClass = Allergy.class)
    @Enumerated(EnumType.STRING)
    private List<Allergy> allergies;

    @ElementCollection(targetClass = Preferences.class)
    public List<Preferences> preferences;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public List<Preferences> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preferences> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return profileCompleted == client.profileCompleted && Objects.equals(clientId, client.clientId) && Objects.equals(login, client.login) && Objects.equals(password, client.password) && Objects.equals(email, client.email) && Objects.equals(phoneNumber, client.phoneNumber) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(address, client.address) && Objects.equals(createdDate, client.createdDate) && Objects.equals(allergies, client.allergies) && Objects.equals(preferences, client.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, login, password, email, phoneNumber, firstName, lastName, address, profileCompleted, createdDate, allergies, preferences);
    }
}
