//package pheninux.xdev.gestork.core.table.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//import org.thymeleaf.TemplateEngine;
//import pheninux.xdev.gestork.core.employee.service.EmployeeService;
//import pheninux.xdev.gestork.core.table.model.TableAssignment;
//import pheninux.xdev.gestork.core.table.repository.TableAssignmentRepository;
//import pheninux.xdev.gestork.utils.Utils;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class TableAssignmentServiceTest {
//
//    @InjectMocks
//    private TableAssignmentService tableAssignmentService;
//
//    @Mock
//    private TableAssignmentRepository tableAssignmentRepository;
//
//    @Mock
//    private EmployeeService employeeService;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAssignedTable() {
//        TableAssignment tableAssignment1 = new TableAssignment();
//        TableAssignment tableAssignment2 = new TableAssignment();
//        when(tableAssignmentRepository.findAll()).thenReturn(Arrays.asList(tableAssignment1, tableAssignment2));
//
//        List<TableAssignment> result = tableAssignmentService.getAssignedTable();
//
//        assertEquals(2, result.size());
//        verify(tableAssignmentRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetAssignedTableByEmployeeId() {
//        TableAssignment tableAssignment1 = new TableAssignment();
//        tableAssignment1.setTableNumber(1);
//        TableAssignment tableAssignment2 = new TableAssignment();
//        tableAssignment2.setTableNumber(2);
//        when(tableAssignmentRepository.findTableAssignmentsByEmployeeId(1L)).thenReturn(Arrays.asList(tableAssignment1, tableAssignment2));
//
//        List<Integer> result = tableAssignmentService.getAssignedTableByEmployeeId(1L);
//
//        assertEquals(Arrays.asList(1, 2), result);
//        verify(tableAssignmentRepository, times(1)).findTableAssignmentsByEmployeeId(1L);
//    }
//
//    @Test
//    void testTableAssignmentOverview() {
//        String tables = "1,2,3";
//        TableAssignment tableAssignment1 = new TableAssignment();
//        tableAssignment1.setTableNumber(1);
//        when(tableAssignmentRepository.findAll()).thenReturn(Collections.singletonList(tableAssignment1));
//
//        Map<String, Set<Integer>> result = tableAssignmentService.tableAssignmentOverview(tables);
//
//        assertEquals(new HashSet<>(Arrays.asList(2, 3)), result.get("unassigned"));
//        assertEquals(new HashSet<>(Collections.singletonList(1)), result.get("assigned"));
//    }
//
//    @Test
//    void testAssignTables_InvalidFormat() {
//        String tables = "invalid";
//        try (var mockedUtils = mockStatic(Utils.class)) {
//            TemplateEngine templateEngine = mock(TemplateEngine.class);
//            mockedUtils.when(() -> Utils.renderAlertSingle("alert-danger", "Le format des tables est invalide")).thenReturn("Le format des tables est invalide");
//            Utils.templateEngine = templateEngine;
//
//            ResponseEntity<String> response = tableAssignmentService.assignTables(1L, tables);
//
//            assertEquals(200, response.getStatusCodeValue());
//            assertTrue(response.getBody().contains("Le format des tables est invalide"));
//        }
//    }
//
////    @Test
////    void testAssignTables_Success() {
////        String tables = "1,2,3";
////        Employee waiter = new Employee();
////        when(employeeService.findEmployeeById(1L)).thenReturn(waiter);
////
////        Map<String, Set<Integer>> tableAssignmentStatus = new HashMap<>();
////        tableAssignmentStatus.put("unassigned", new HashSet<>(Arrays.asList(2, 3)));
////        tableAssignmentStatus.put("assigned", new HashSet<>(Collections.singletonList(1)));
////
////        when(tableAssignmentService.tableAssignmentOverview(tables)).thenReturn(tableAssignmentStatus);
////
////        try (var mockedUtils = mockStatic(Utils.class)) {
////            TemplateEngine templateEngine = mock(TemplateEngine.class);
////            mockedUtils.when(() -> Utils.isValidTableFormat(tables)).thenReturn(true);
////            mockedUtils.when(() -> Utils.renderAlertSingle("alert-danger", "Le format des tables est invalide")).thenReturn("Le format des tables est invalide");
////            Utils.templateEngine = templateEngine;
////
////            ResponseEntity<String> response = tableAssignmentService.assignTables(1L, tables);
////
////            assertEquals(200, response.getStatusCodeValue());
////            assertTrue(response.getBody().contains("Ces tables viennent d'être assignées"));
////            verify(tableAssignmentRepository, times(3)).save(any(TableAssignment.class));
////        }
////    }
//
//    @Test
//    void testAssignTables_Exception() {
//        String tables = "1,2,3";
//        when(employeeService.findEmployeeById(1L)).thenThrow(new RuntimeException("Error"));
//
//        ResponseEntity<String> response = tableAssignmentService.assignTables(1L, tables);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertTrue(response.getBody().contains("Error while assigning tables to waiter"));
//    }
//}