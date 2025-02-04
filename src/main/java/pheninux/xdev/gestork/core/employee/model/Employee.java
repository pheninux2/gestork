package pheninux.xdev.gestork.core.employee.model;


import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId; // Identifiant unique, clé primaire

    private String name; // Nom de l'employé
    private String email; // Adresse e-mail de l'employé
    private String phoneNumber; // Numéro de téléphone de l'employé

    @Enumerated(EnumType.STRING)
    private EmployeeRole role; // Rôle de l'employé (ADMIN, SERVER, CHEF)

    private String login; // Identifiant de connexion
    private String password; // Mot de passe (généré lors de l'embauche)

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin; // Date de la dernière connexion

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate; // Date de création du compte

    // Constructeur par défaut
    public Employee() {
        this.createdDate = new Date(); // Initialiser la date de création à la date actuelle
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole employeeRole) {
        this.role = employeeRole;
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

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId) && Objects.equals(name, employee.name) && Objects.equals(email, employee.email) && Objects.equals(phoneNumber, employee.phoneNumber) && role == employee.role && Objects.equals(login, employee.login) && Objects.equals(password, employee.password) && Objects.equals(lastLogin, employee.lastLogin) && Objects.equals(createdDate, employee.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, name, email, phoneNumber, role, login, password, lastLogin, createdDate);
    }
}
