package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("userMvcController")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        return "profile";
    }
}
