package pheninux.xdev.gestork.web.accesscode.view;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;
import static pheninux.xdev.gestork.utils.Utils.isWaiter;

@Controller
@RequestMapping("/view/code")
public class CodeViewController {

    @GetMapping("/generate")
    public String displayGenerateCodePage(HttpServletResponse response) {
        if (!isAdmin() && !isWaiter()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "error/403";
        }
        return "employee/waiter/layout/generateCode";
    }

    @GetMapping("/display")
    public String displayCodePage(@RequestParam("code") String code, Model model) {
        model.addAttribute("accessCode", code);
        return "employee/waiter/layout/displayCode";
    }
}
