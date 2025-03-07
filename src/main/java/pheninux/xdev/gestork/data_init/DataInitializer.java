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
        String sql = "INSERT INTO EMPLOYEE (CREATED_DATE, EMAIL, LAST_LOGIN, LOGIN, NAME, PASSWORD, PHONE_NUMBER, ROLE) VALUES " +
                "('2025-02-17 15:20:46.000000', 'adil.haddad.xdev@gmail.com', '2025-02-17 15:23:29.000000', 'ahaddad', 'adil', '$2a$10$ZLU.BErNo3FUThdgutCzKO6kklyd4.1etaM82Z/LktCN.fvmJE9dy', '0745365899', 'WAITER')";
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
