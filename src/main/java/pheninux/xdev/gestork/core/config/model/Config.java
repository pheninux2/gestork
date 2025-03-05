package pheninux.xdev.gestork.core.config.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean permitTableAssignmentToAll;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public boolean isPermitTableAssignmentToAll() {
        return permitTableAssignmentToAll;
    }

    public void setPermitTableAssignmentToAll(boolean permitTableAssignmentToAll) {
        this.permitTableAssignmentToAll = permitTableAssignmentToAll;
    }
}
