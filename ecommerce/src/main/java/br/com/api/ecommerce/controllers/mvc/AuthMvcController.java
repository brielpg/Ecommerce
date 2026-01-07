package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Address.AddressDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AuthMvcController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPost() {
        return "redirect:/";
    }

    @GetMapping("/registrar")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/registrar")
    public String registerPost(
            String name,
            String email,
            String password,
            String phone,
            String birthDate,
            String streetName,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String zipCode,
            RedirectAttributes redirectAttributes) {
        try {
            List<AddressDtoCreate> addresses = List.of(new AddressDtoCreate(
                    streetName, number, complement, neighborhood, city, state, zipCode));
            UserDtoCreate userDto = new UserDtoCreate(
                    name, email, password, phone, LocalDate.parse(birthDate), addresses);
            userService.create(userDto);
            redirectAttributes.addFlashAttribute("success", "Usuário registrado com sucesso! Faça login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao registrar usuário: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
