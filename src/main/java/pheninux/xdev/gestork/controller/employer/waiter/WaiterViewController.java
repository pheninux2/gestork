package pheninux.xdev.gestork.controller.employer.waiter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static pheninux.xdev.gestork.utils.Utils.isAdmin;
import static pheninux.xdev.gestork.utils.Utils.isWaiter;

@Controller
@RequestMapping("/waiter")
public class WaiterViewController {

    @GetMapping("/generateCode-page")
    public String displayGenerateCodePage() {
        if (!isAdmin() && !isWaiter()) {
            return "error/403";
        }
        return "employee/waiter/services/generate-code";
    }

    @GetMapping("/showAccessCode-page")
    public String displayAccessCodePage(@RequestParam("code") String code, Model model) {
        model.addAttribute("accessCode", code);
        return "employee/waiter/show-access-code";
    }
}
