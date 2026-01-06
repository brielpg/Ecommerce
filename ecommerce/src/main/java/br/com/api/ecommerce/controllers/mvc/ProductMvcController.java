package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.services.ProductService;
import br.com.api.ecommerce.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/products")
@PreAuthorize("permitAll()")
public class ProductMvcController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "q", required = false) String query,
                                 Pageable pageable,
                                 Model model) {

        Page<ProductDtoList> products = productService.search(query, pageable);

        model.addAttribute("products", products);
        model.addAttribute("query", query);

        return "product-search";
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
        return "product-details";
    }
}
