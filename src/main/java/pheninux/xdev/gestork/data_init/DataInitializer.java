package pheninux.xdev.gestork.data_init;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;  // Pour exécuter des requêtes SQL directement

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Insérer dans la table CONFIG
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CONFIG (PERMIT_TABLE_ASSIGNMENT_TO_ALL) VALUES (false)").executeUpdate();

        // Insérer dans la table EMPLOYEE
        entityManager.createNativeQuery("INSERT INTO PUBLIC.EMPLOYEE (CREATED_DATE, EMAIL, LAST_LOGIN, LOGIN, NAME, PASSWORD, PHONE_NUMBER, ROLE) VALUES ('2025-02-17 15:20:46.000000', 'adil.haddad.xdev@gmail.com', '2025-02-17 15:23:29.000000', 'ahaddad', 'adil', '$2a$10$ZLU.BErNo3FUThdgutCzKO6kklyd4.1etaM82Z/LktCN.fvmJE9dy', '0745365899', 'WAITER')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.EMPLOYEE (CREATED_DATE, EMAIL, LAST_LOGIN, LOGIN, NAME, PASSWORD, PHONE_NUMBER, ROLE) VALUES ('2025-02-17 15:20:46.000000', 'sherine@gmail.com', '2025-02-17 15:23:29.000000', 'shaddad', 'sherine', '$2a$10$ZLU.BErNo3FUThdgutCzKO6kklyd4.1etaM82Z/LktCN.fvmJE9dy', '0659857454', 'WAITER')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.EMPLOYEE (CREATED_DATE, EMAIL, LAST_LOGIN, LOGIN, NAME, PASSWORD, PHONE_NUMBER, ROLE) VALUES ('2025-03-17 15:20:46.000000', 'adil.haddad.xdev@gmail.com', '2025-02-17 15:23:29.000000', 'admin', 'admin', '$2a$10$ZLU.BErNo3FUThdgutCzKO6kklyd4.1etaM82Z/LktCN.fvmJE9dy', '0745365899', 'ADMIN')").executeUpdate();
        // Insérer dans la table CUSTOMER_TABLE
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (4, 1, 'BUSY')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (4, 2, 'AVAILABLE')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (4, 3, 'AVAILABLE')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (6, 4, 'AVAILABLE')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (6, 5, 'BUSY')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (6, 6, 'AVAILABLE')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.CUSTOMER_TABLE (CAPACITE, NUMERO_TABLE, STATUT) VALUES (6, 7, 'AVAILABLE')").executeUpdate();

        // Insérer dans la table TABLE_ASSIGNMENT
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (1, 1)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (2, 1)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (3, 1)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (5, 2)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (6, 2)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO PUBLIC.TABLE_ASSIGNMENT (TABLE_NUMBER, EMPLOYEE_ID) VALUES (7, 2)").executeUpdate();


    }
}