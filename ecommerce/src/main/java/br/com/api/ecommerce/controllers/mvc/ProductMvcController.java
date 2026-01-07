package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.Category;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.services.ProductService;
import br.com.api.ecommerce.services.ReviewService;
import br.com.api.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/produtos")
public class ProductMvcController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/buscar")
    public String searchProducts(@RequestParam(value = "q", required = false) String query, Pageable pageable, Model model, @AuthenticationPrincipal User user) {
        Page<ProductDtoList> products = productService.search(query, pageable);

        List<UUID> favoriteIds = new ArrayList<>();
        if (user != null) {
            List<ProductDtoList> favorites = userService.getFavorites(user.getId());
            favoriteIds = favorites.stream().map(ProductDtoList::id).toList();
        }
        model.addAttribute("favoriteIds", favoriteIds);
        model.addAttribute("products", products);
        model.addAttribute("query", query);

        return "product/search";
    }

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable UUID id, Pageable pageable, Model model, @AuthenticationPrincipal User user) {
        ProductDtoList product = productService.entityToDto(productService.getById(id));
        Page<CategoryDtoList> categories = categoryService.getAllByProductId(id, pageable);
        Page<ReviewDtoList> reviews = reviewService.getAllByProduct(id, pageable);

        if (user != null) {
            model.addAttribute("currentUserId", user.getId());
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("reviews", reviews);
        return "product/details";
    }

    @GetMapping("/categoria/{categoryId}")
    public String getProductsByCategory(@PathVariable UUID categoryId, Pageable pageable, Model model) {
        Page<ProductDtoList> products = productService.getAllByCategoryId(categoryId, pageable);
        Category category = categoryService.getById(categoryId);

        model.addAttribute("products", products);
        model.addAttribute("categoryName", category.getName());

        return "product/category";
    }
}
