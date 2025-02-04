package pheninux.xdev.gestork.core.table.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pheninux.xdev.gestork.core.employee.model.Employee;
import pheninux.xdev.gestork.core.employee.service.EmployeeService;
import pheninux.xdev.gestork.core.table.model.EmployeeTableAssignment;
import pheninux.xdev.gestork.core.table.repository.EmployeeTableAssignmentRepository;
import pheninux.xdev.gestork.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static pheninux.xdev.gestork.utils.Utils.isValidTableFormat;
import static pheninux.xdev.gestork.utils.Utils.parseTableNumbers;

@Service
public class EmployeeTableAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeTableAssignmentService.class);
    private final EmployeeTableAssignmentRepository employeeTableAssignmentRepository;
    private final EmployeeService employeeService;

    public EmployeeTableAssignmentService(EmployeeTableAssignmentRepository employeeTableAssignmentRepository, EmployeeService employeeService) {
        this.employeeTableAssignmentRepository = employeeTableAssignmentRepository;
        this.employeeService = employeeService;
    }

    public List<EmployeeTableAssignment> getAssignedTable() {
        return employeeTableAssignmentRepository.findAll();
    }

    public Map<String, Set<Integer>> tableAssignmentOverview(String tables) {
        Set<Integer> tablesToBeAssigned = new HashSet<>(parseTableNumbers(tables));
        Set<Integer> assignedTablesFromDataBase = getAssignedTable()
                .stream()
                .map(EmployeeTableAssignment::getTableNumber)
                .collect(Collectors.toSet());

        Set<Integer> unassignedTables = tablesToBeAssigned
                .stream()
                .filter(t -> !assignedTablesFromDataBase.contains(t))
                .collect(Collectors.toSet());

        Set<Integer> assignedTableFromGiven = tablesToBeAssigned
                .stream()
                .filter(t -> assignedTablesFromDataBase.contains(t))
                .collect(Collectors.toSet());

        Map<String, Set<Integer>> tableAssignmentStatus = new HashMap<>();
        if (!unassignedTables.isEmpty()) {
            tableAssignmentStatus.put("unassigned", unassignedTables);
        }
        if (!assignedTableFromGiven.isEmpty()) {
            tableAssignmentStatus.put("assigned", assignedTableFromGiven);
        }

        return tableAssignmentStatus;
    }

    public ResponseEntity<String> assignEmployeeToTable(Long waiterId, String tables) {

        if (!isValidTableFormat(tables)) {
            return ResponseEntity.status(200).body(Utils.renderAlertSingle("alert-danger", "Le format des tables est invalide"));
        }

        try {
            Optional<Employee> waiter = Optional.ofNullable(employeeService.findEmployeeById(waiterId));
            Map<String, Set<Integer>> tableAssignmentStatus = tableAssignmentOverview(tables);
            Set<Integer> unassignedTables = tableAssignmentStatus.get("unassigned");
            Set<Integer> assignedTables = tableAssignmentStatus.get("assigned");

            assignTablesToWaiter(waiter, unassignedTables);

            return buildResponseMessage(unassignedTables, assignedTables);
        } catch (Exception e) {
            log.error("Error while assigning tables to waiter", e);
            return ResponseEntity.status(200).body("Error while assigning tables to waiter");
        }
    }

    private void assignTablesToWaiter(Optional<Employee> waiter, Set<Integer> unassignedTables) {
        if (unassignedTables != null && !unassignedTables.isEmpty()) {
            waiter.ifPresent(employee -> unassignedTables.forEach(tableNumber -> {
                EmployeeTableAssignment employeeTableAssignment = new EmployeeTableAssignment();
                employeeTableAssignment.setEmployee(employee);
                employeeTableAssignment.setTableNumber(tableNumber);
                employeeTableAssignmentRepository.save(employeeTableAssignment);
                log.info("Tables {} assigned to waiter successfully", tableNumber);
            }));
        }
    }

    private ResponseEntity<String> buildResponseMessage(Set<Integer> unassignedTables, Set<Integer> assignedTables) {
        String message;

        Boolean isUnassignedTablesNotEmpty = unassignedTables != null && !unassignedTables.isEmpty();
        Boolean isAssignedTablesNotEmpty = assignedTables != null && !assignedTables.isEmpty();

        if (isUnassignedTablesNotEmpty && isAssignedTablesNotEmpty) {
            List<String> messages = Arrays.asList(
                    "Ces tables viennent d'être assignées : " + unassignedTables,
                    "Ces tables sont déjà assignées : " + assignedTables
            );
            message = Utils.renderAlertMultiple("alert-warning-multiple", messages);
        } else if (isUnassignedTablesNotEmpty) {
            message = Utils.renderAlertSingle("alert-success",
                    "Ces tables viennent d'être assignées  " + unassignedTables
            );
        } else if (isAssignedTablesNotEmpty) {
            message = Utils.renderAlertSingle("alert-warning-single",
                    "Ces tables sont déjà assignées : " + assignedTables);
        } else {
            message = Utils.renderAlertSingle("alert-warning", "Aucune table n'a été assignée.");
        }

        return ResponseEntity.status(200).body(message);

    }
}