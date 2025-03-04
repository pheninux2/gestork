package pheninux.xdev.gestork.core.accessCode.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;

public interface AccessCodeRepository extends JpaRepository<AccessCode, String> {
    AccessCode findByCode(String code);

    @Modifying
    @Transactional
    @Query("update AccessCode ac set ac.used = true where ac.code = :code")
    void updateAccessCodeToUsed(String code);
}
