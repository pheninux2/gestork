package pheninux.xdev.gestork.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.accessCode.model.AccessCode;
import pheninux.xdev.gestork.core.customer.model.Customer;
import pheninux.xdev.gestork.core.accessCode.repository.AccessCodeRepository;
import pheninux.xdev.gestork.core.customer.repository.CustomerRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomClientDetailsService implements UserDetailsService {


    private final CustomerRepository customerRepository;
    private final AccessCodeRepository accessCodeRepository;

    public CustomClientDetailsService(CustomerRepository customerRepository, AccessCodeRepository accessCodeRepository) {
        this.customerRepository = customerRepository;
        this.accessCodeRepository = accessCodeRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Customer> client = Optional.ofNullable(customerRepository.findClientByLogin(login));

        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    client.get().getLogin(),
                    client.get().getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + client.get().getRole()))
            );
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé pour login : " + login);
        }
    }

    public UserDetails loadUserByUsername(String login, String code) throws UsernameNotFoundException {
        Customer customer = customerRepository.findClientByLogin(login);
        AccessCode accessCode = accessCodeRepository.findByCode(code);

        if (customer != null
                && accessCode != null
                && accessCode.getCode().equals(code)
                && !accessCode.isUsed()) {
            Timestamp expiryDate = accessCode.getExpiryDate();
            Timestamp currentDate = Timestamp.valueOf(LocalDateTime.now());
            boolean isTokenValid = expiryDate.after(currentDate);

            if (isTokenValid) {
                accessCode.setUsed(Boolean.TRUE);
                accessCodeRepository.save(accessCode);
                return new org.springframework.security.core.userdetails.User(
                        customer.getLogin(),
                        customer.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole()))
                );
            } else {
                throw new BadCredentialsException("Token was expired");
            }
        }
        throw new BadCredentialsException("Invalid credentials");
    }
}
