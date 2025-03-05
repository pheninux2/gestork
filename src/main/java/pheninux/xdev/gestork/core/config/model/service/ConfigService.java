package pheninux.xdev.gestork.core.config.model.service;

import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.config.model.repository.ConfigRepository;

@Service
public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public boolean permitTableAssignmentToAll() {
        return configRepository.findAll().getFirst().isPermitTableAssignmentToAll();
    }

}
