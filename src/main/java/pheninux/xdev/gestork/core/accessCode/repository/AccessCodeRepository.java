package pheninux.xdev.gestork.core.accessCode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;

public interface AccessCodeRepository extends JpaRepository<AccessCode, String> {
    AccessCode findByCode(String code);
}
