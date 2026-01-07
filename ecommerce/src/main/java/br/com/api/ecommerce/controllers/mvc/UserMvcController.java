package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.AddressService;
import br.com.api.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserMvcController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        List<ProductDtoList> favorites = userService.getFavorites(user.getId());

        model.addAttribute("favoriteIds", favorites.stream().map(ProductDtoList::id).toList());
        model.addAttribute("favorites", favorites);
        return "favorites";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        var addresses = addressService.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);

        return "profile";
    }
}
