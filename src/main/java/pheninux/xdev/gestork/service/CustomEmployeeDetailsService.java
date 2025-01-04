package pheninux.xdev.gestork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.Employee;
import pheninux.xdev.gestork.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomEmployeeDetailsService.class);

    private final EmployeeRepository employeeRepository;


    @Autowired
    private BruteForceProtectionService bruteForceProtectionService;

    public CustomEmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Employee> employee = Optional.ofNullable(employeeRepository.findEmployeeByLogin(login));

        if (employee.isEmpty()) {
            throw new UsernameNotFoundException("Employé non trouvé pour login : " + login);
        }

        Employee emp = employee.get();

        boolean isValidCredentials = bruteForceProtectionService.validateCredentials(emp.getLogin(), emp.getPassword());

        if (!isValidCredentials) {
            throw new BadCredentialsException("Identifiants invalides");
        }

        return new org.springframework.security.core.userdetails.User(
                emp.getLogin(),
                emp.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + emp.getRole().name()))
        );
    }
}



