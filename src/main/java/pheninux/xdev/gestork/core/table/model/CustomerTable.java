package pheninux.xdev.gestork.core.table.model;

import jakarta.persistence.*;

@Entity
public class CustomerTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numeroTable;
    private int capacite;
    @Enumerated(EnumType.STRING)
    private TableStatus statut;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public TableStatus getStatut() {
        return statut;
    }

    public void setStatut(TableStatus statut) {
        this.statut = statut;
    }

    public int getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(int numeroTable) {
        this.numeroTable = numeroTable;
    }

}
