package pheninux.xdev.gestork.core.employee.model.dto;

import pheninux.xdev.gestork.core.employee.model.Employee;

import java.util.List;

public class AssignedTableDto {

    private Employee waiter;
    private List<Integer> tables;

    public AssignedTableDto() {

    }

    public AssignedTableDto(Employee waiter, List<Integer> tables) {
        this.waiter = waiter;
        this.tables = tables;
    }

    public Employee getWaiter() {
        return waiter;
    }

    public void setWaiter(Employee waiter) {
        this.waiter = waiter;
    }

    public List<Integer> getTables() {
        return tables;
    }

    public void setTables(List<Integer> tables) {
        this.tables = tables;
    }
}
