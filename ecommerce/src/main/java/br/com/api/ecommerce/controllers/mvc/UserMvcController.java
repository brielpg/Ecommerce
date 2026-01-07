package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserMvcController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model, Pageable pageable) {
        Page<ProductDtoList> products = productService.getAll(pageable);
        List<ProductDtoList> bestSellers = productService.getBestSellers(10);
        Page<CategoryDtoList> categories = categoryService.getAll(pageable);

        List<UUID> favoriteIds = new ArrayList<>();
        if (user != null) {
            List<ProductDtoList> favorites = userService.getFavorites(user.getId());
            favoriteIds = favorites.stream().map(ProductDtoList::id).toList();
        }

        model.addAttribute("products", products);
        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("categories", categories);
        model.addAttribute("favoriteIds", favoriteIds);
        return "home";
    }

    @GetMapping("/carrinho")
    public String cart(@AuthenticationPrincipal User user, Model model) {
        Cart cart = cartService.getByUserId(user.getId());
        CartDtoList cartDto = cartService.entityToDto(cart);

        model.addAttribute("cart", cartDto);
        model.addAttribute("userId", user.getId());

        return "user/cart";
    }

    @GetMapping("/favoritos")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        List<ProductDtoList> favorites = userService.getFavorites(user.getId());

        model.addAttribute("favoriteIds", favorites.stream().map(ProductDtoList::id).toList());
        model.addAttribute("favorites", favorites);
        return "user/favorites";
    }

    @GetMapping("/perfil")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        var addresses = addressService.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);

        return "user/profile";
    }
}
