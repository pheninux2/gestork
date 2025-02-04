package pheninux.xdev.gestork.core.table.model;

import jakarta.persistence.*;
import pheninux.xdev.gestork.core.employee.model.Employee;

@Entity
public class EmployeeTableAssignment {


    public EmployeeTableAssignment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    @Column(nullable = false)
    private int tableNumber;


    public EmployeeTableAssignment(Employee employee, int tableNumber) {
        this.employee = employee;
        this.tableNumber = tableNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }


}
