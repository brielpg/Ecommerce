package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.services.AddressService;
import br.com.api.ecommerce.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class UserMvcController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        List<ProductDtoList> favorites = userService.getFavorites(user.getId());
        model.addAttribute("favorites", favorites);
        return "favorites";
    }

    @PostMapping("/favorites/{productId}")
    public String toggleFavorite(@AuthenticationPrincipal User user, @PathVariable UUID productId, HttpServletRequest request) {
        List<ProductDtoList> favorites = userService.getFavorites(user.getId());
        boolean isFavorite = favorites.stream().anyMatch(p -> p.id().equals(productId));

        if (isFavorite) {
            userService.removeFavorite(user.getId(), productId);
        } else {
            userService.addFavorite(user.getId(), productId);
        }

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        var addresses = addressService.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);

        return "profile";
    }
}
