package pheninux.xdev.gestork.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Utils {

    private static TemplateEngine templateEngine = null;

    public final static List<SseEmitter> waiterEmitters = new CopyOnWriteArrayList<>();
    public final static List<SseEmitter> customerEmitters = new CopyOnWriteArrayList<>();

    public Utils(TemplateEngine templateEngine) {
        Utils.templateEngine = templateEngine;
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public static boolean isWaiter() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_WAITER"));
    }

    public static boolean isChef() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CHEF"));
    }

    public static boolean isCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"));
    }

    public static boolean isManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"));
    }


    public static String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    public static String renderAlertSingle(String fragmentName, String message) {
        Context context = new Context();
        context.setVariable("message", message);
        Set<String> fragmentSelectors = new HashSet<>();
        fragmentSelectors.add(fragmentName);
        return templateEngine.process("alert/alerts", fragmentSelectors, context);
    }

    public static String renderAlertMultiple(String fragmentName, List<String> messages) {
        Context context = new Context();
        context.setVariable("messages", messages);
        Set<String> fragmentSelectors = new HashSet<>();
        fragmentSelectors.add(fragmentName);
        return templateEngine.process("alert/alerts", fragmentSelectors, context);
    }

    public static Set<Integer> parseTableNumbers(String tableNumbers) {
        Set<Integer> tables = new HashSet<>();
        String[] tableNumbersArray = tableNumbers.split(",");
        for (String tableNumber : tableNumbersArray) {
            tables.add(Integer.parseInt(tableNumber));
        }
        return tables;
    }

    public static int getTableNumber(String code) {
        String[] codeParts = code.split("-");
        return Integer.parseInt(codeParts[1]);
    }

    public static boolean isValidTableFormat(String tables) {
        String regex = "^\\d+(,\\d+)*$";
        return tables != null && tables.matches(regex);
    }

    public static void notifyWaiters() {
        waiterEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data("New order created"));
            } catch (Exception e) {
                emitter.completeWithError(e);

            }
        });
    }

    public static void notifyCustomer() {
        customerEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data("changed status"));
            } catch (Exception e) {
                emitter.completeWithError(e);

            }
        });
    }

}
