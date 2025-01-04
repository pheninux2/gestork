package pheninux.xdev.gestork.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.model.Client;
import pheninux.xdev.gestork.repository.ClientRepository;

import java.util.Collections;

@Service
public class CustomClientDetailsService implements UserDetailsService {


    private final ClientRepository clientRepository;

    public CustomClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Client client = clientRepository.findClientByLogin(login);
        if (client == null) {
            throw new UsernameNotFoundException("Client non trouvé");
        }
        return new org.springframework.security.core.userdetails.User(
                client.getLogin(),
                client.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("CLIENT")
                ));
    }
}
