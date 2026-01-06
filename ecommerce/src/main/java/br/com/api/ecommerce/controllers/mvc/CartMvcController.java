package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import br.com.api.ecommerce.models.dtos.Item.DtoItemRequest;
import br.com.api.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class CartMvcController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        Cart cart = cartService.getByUserId(user.getId());
        CartDtoList cartDto = cartService.entityToDto(cart);

        model.addAttribute("cart", cartDto);

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@AuthenticationPrincipal User user,
                            @RequestParam UUID productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            RedirectAttributes redirectAttributes) {
        try {
            DtoItemRequest itemRequest = new DtoItemRequest(productId, quantity);
            cartService.addItemsToUserCart(user.getId(), List.of(itemRequest));
            redirectAttributes.addFlashAttribute("success", "Produto adicionado ao carrinho!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao adicionar produto ao carrinho: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@AuthenticationPrincipal User user,
                                 @RequestParam UUID productId,
                                 @RequestParam Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            DtoItemRequest itemRequest = new DtoItemRequest(productId, quantity);
            cartService.removeItemsFromUserCart(user.getId(), List.of(itemRequest));
            redirectAttributes.addFlashAttribute("success", "Produto removido do carrinho!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao remover produto do carrinho: " + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(@AuthenticationPrincipal User user,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.clearUserCart(user.getId());
            redirectAttributes.addFlashAttribute("success", "Carrinho limpo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao limpar carrinho: " + e.getMessage());
        }
        return "redirect:/cart";
    }
}
