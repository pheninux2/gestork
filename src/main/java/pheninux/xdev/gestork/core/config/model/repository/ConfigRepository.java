package pheninux.xdev.gestork.core.config.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pheninux.xdev.gestork.core.config.model.Config;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}
