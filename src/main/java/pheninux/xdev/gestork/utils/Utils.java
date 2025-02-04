package pheninux.xdev.gestork.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Utils {

    private static TemplateEngine templateEngine = null;

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
        Set<String> fragmentsSelectors = new HashSet<>();
        fragmentsSelectors.add(fragmentName);
        return templateEngine.process("alert/alerts", fragmentsSelectors, context);
    }

    public static String renderAlertMultiple(String fragmentName, List<String> messages) {
        Context context = new Context();
        context.setVariable("messages", messages);
        Set<String> fragmentsSelectors = new HashSet<>();
        fragmentsSelectors.add(fragmentName);
        return templateEngine.process("alert/alerts", fragmentsSelectors, context);
    }

    public static Set<Integer> parseTableNumbers(String tableNumbers) {
        Set<Integer> tables = new HashSet<>();
        String[] tableNumbersArray = tableNumbers.split(",");
        for (String tableNumber : tableNumbersArray) {
            tables.add(Integer.parseInt(tableNumber));
        }
        return tables;
    }

    public static boolean isValidTableFormat(String tables) {
        String regex = "^\\d+(,\\d+)*$";
        return tables != null && tables.matches(regex);
    }
}
