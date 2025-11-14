package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import br.com.api.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("cartMvcController")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        Cart cart = cartService.getByUserId(user.getId());
        CartDtoList cartDto = cartService.entityToDto(cart);

        model.addAttribute("cart", cartDto);

        return "cart";
    }
}
