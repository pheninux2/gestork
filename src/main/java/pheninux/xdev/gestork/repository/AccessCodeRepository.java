package pheninux.xdev.gestork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pheninux.xdev.gestork.model.AccessCode;

public interface AccessCodeRepository extends JpaRepository<AccessCode, String> {
    AccessCode findByCode(String code);
}
