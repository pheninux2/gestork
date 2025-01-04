package pheninux.xdev.gestork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pheninux.xdev.gestork.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByLogin(String login);
}
