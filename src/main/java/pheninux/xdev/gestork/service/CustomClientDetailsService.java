package pheninux.xdev.gestork.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.AccessCode;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.repository.AccessCodeRepository;
import pheninux.xdev.gestork.repository.ClientRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomClientDetailsService implements UserDetailsService {


    private final ClientRepository clientRepository;
    private final AccessCodeRepository accessCodeRepository;

    public CustomClientDetailsService(ClientRepository clientRepository, AccessCodeRepository accessCodeRepository) {
        this.clientRepository = clientRepository;
        this.accessCodeRepository = accessCodeRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Client> client = Optional.ofNullable(clientRepository.findClientByLogin(login));

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
        Client client = clientRepository.findClientByLogin(login);
        AccessCode accessCode = accessCodeRepository.findByCode(code);

        if (client != null
                && accessCode != null
                && (accessCode.getCode().equals(code))
                && accessCode.getExpiryDate().after(Timestamp.valueOf(LocalDateTime.now()))) {
            return new org.springframework.security.core.userdetails.User(
                    client.getLogin(),
                    client.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + client.getRole()))
            );

        } else {
            throw new BadCredentialsException("Invalid credentials");        }
    }
}
