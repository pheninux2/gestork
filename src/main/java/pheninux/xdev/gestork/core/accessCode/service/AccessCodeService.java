package pheninux.xdev.gestork.core.accessCode.service;

import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;
import pheninux.xdev.gestork.core.accessCode.repository.AccessCodeRepository;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;
import pheninux.xdev.gestork.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessCodeService {

    private final AccessCodeRepository accessCodeRepository;
    private final EmployeeRepository employeeRepository;


    public AccessCodeService(AccessCodeRepository accessCodeRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.accessCodeRepository = accessCodeRepository;
        this.employeeRepository = employeeRepository;
    }

    public String generateAndSaveAccessCode(Customer customer, int tableNumber) {

        try {
            Employee waiter = employeeRepository.findEmployeeByLogin(Utils.getLogin());
            String code = generateAccessCode(tableNumber);
            Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusMinutes(6)); // Code valide 1 heure
            AccessCode accessCode = new AccessCode();
            accessCode.setCustomer(customer);
            accessCode.setEmployee(waiter);
            accessCode.setCode(code);
            accessCode.setExpiryDate(expiryDate);
            accessCode.setUsed(false);

            accessCodeRepository.saveAndFlush(accessCode);
            return code;
        } catch (Exception e) {
            throw new RuntimeException("Error to save generating access code", e);
        }
    }

    public String generateAccessCode(int tableNumber) {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");

        StringBuilder accessCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            char randomChar = uuidString.charAt((int) (Math.random() * uuidString.length()));
            accessCode.append(randomChar);
        }

        accessCode.append("-");
        accessCode.append(tableNumber);

        return accessCode.toString();
    }

    public boolean isAccessCodeValid(String code) {
        AccessCode accessCode = accessCodeRepository.findByCode(code);
        if (accessCode == null || accessCode.isUsed() || accessCode.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }

        return true;
    }
}
