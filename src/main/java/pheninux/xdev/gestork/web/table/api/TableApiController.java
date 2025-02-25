package pheninux.xdev.gestork.web.table.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pheninux.xdev.gestork.core.table.service.TableAssignmentService;

import java.net.URI;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;

@Controller
@RequestMapping("/api/table")
public class TableApiController {

    private final TableAssignmentService tableAssignmentService;

    public TableApiController(TableAssignmentService tableAssignmentService) {
        this.tableAssignmentService = tableAssignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignTables(@RequestParam("waiter") Long waiterId, @RequestParam("tables") String tables) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .location(URI.create("/error/403"))
                    .build();
        }
        return tableAssignmentService.assignTables(waiterId, tables);

    }
}