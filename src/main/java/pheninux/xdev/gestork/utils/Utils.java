package pheninux.xdev.gestork.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashSet;
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

    public static String renderAlert(String fragmentName, String message) {
        Context context = new Context();
        context.setVariable("message", message);
        Set<String> fragmentsSelectors = new HashSet<>();
        fragmentsSelectors.add(fragmentName);
        return templateEngine.process("alert/alerts", fragmentsSelectors, context);
    }
}
