package pheninux.xdev.gestork.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomClientDetailsService implements UserDetailsService {


    private final ClientRepository clientRepository;

    public CustomClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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
}
