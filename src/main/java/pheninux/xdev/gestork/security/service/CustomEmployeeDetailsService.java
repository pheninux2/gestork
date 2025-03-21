package pheninux.xdev.gestork.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.repository.EmployeeRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService , UserDetails {

    private static final Logger log = LoggerFactory.getLogger(CustomEmployeeDetailsService.class);

    private final EmployeeRepository employeeRepository;


    public CustomEmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Employee> employee = Optional.ofNullable(employeeRepository.findEmployeeByLogin(login));

        if (employee.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    employee.get().getLogin(),
                    employee.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + employee.get().getRole()))
            );
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé pour login : " + login);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}



