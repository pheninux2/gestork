package pheninux.xdev.gestork.web.employee.waiter.api;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pheninux.xdev.gestork.core.table.service.TableService;
import pheninux.xdev.gestork.utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static pheninux.xdev.gestork.utils.Utils.renderAlertSingle;

class CodeApiControllerTest {

    @InjectMocks
    private CodeApiController codeApiController;

    @Mock
    private TableService tableService;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCode_Admin() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(true);
            mockedUtils.when(Utils::isWaiter).thenReturn(false);

            String clientLogin = "client1";
            int tableNumber = 1;
            ResponseEntity<String> expectedResponse = new ResponseEntity<>("Code generated", HttpStatus.OK);

            when(tableService.generateCode(clientLogin, tableNumber)).thenReturn(expectedResponse);

            ResponseEntity<String> responseEntity = codeApiController.generateCode(clientLogin, tableNumber, response);

            assertEquals(expectedResponse, responseEntity);
        }
    }

    @Test
    void testGenerateCode_Waiter() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);
            mockedUtils.when(Utils::isWaiter).thenReturn(true);

            String clientLogin = "client2";
            int tableNumber = 2;
            ResponseEntity<String> expectedResponse = new ResponseEntity<>("Code generated", HttpStatus.OK);

            when(tableService.generateCode(clientLogin, tableNumber)).thenReturn(expectedResponse);

            ResponseEntity<String> responseEntity = codeApiController.generateCode(clientLogin, tableNumber, response);

            assertEquals(expectedResponse, responseEntity);
        }
    }

    @Test
    void testGenerateCode_Forbidden() {
        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::isAdmin).thenReturn(false);
            mockedUtils.when(Utils::isWaiter).thenReturn(false);

            String clientLogin = "client3";
            int tableNumber = 3;
            String expectedAlert = renderAlertSingle("alert-danger", "Vous n'êtes pas autorisé à accéder à cette ressource.");
            ResponseEntity<String> expectedResponse = new ResponseEntity<>(expectedAlert, HttpStatus.OK);

            ResponseEntity<String> responseEntity = codeApiController.generateCode(clientLogin, tableNumber, response);

            assertEquals(expectedResponse, responseEntity);
        }
    }
}