package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoForm;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoForm;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.services.ProductService;
import br.com.api.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String admin(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDtoList> users = userService.getAll(pageable);

        Pageable recentPageable = PageRequest.of(0, 7, Sort.by("timestamp").descending());
        Page<UserDtoList> recentUsers = userService.getAll(recentPageable);

        model.addAttribute("users", users);
        model.addAttribute("recentUsers", recentUsers);
        model.addAttribute("currentPage", users.getNumber());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalCategories", categoryService.getAll(PageRequest.of(0, 100)));
        model.addAttribute("totalProducts", productService.getAll(PageRequest.of(0, 100)));

        return "admin";
    }

    @GetMapping("/categories")
    public String categories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<CategoryDtoList> categories = categoryService.getAll(pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", categories.getNumber());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("categoryDto", new CategoryDtoCreate("", ""));

        return "admin_categories";
    }

    @GetMapping("/products")
    public String products(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDtoList> products = productService.getAll(pageable);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("productDto", new ProductDtoCreate("", "", null, null, List.of()));
        model.addAttribute("allCategories", categoryService.getAll(PageRequest.of(0, 100)).getContent());

        return "admin_products";
    }

    @GetMapping("/users")
    public String listUsers(Pageable pageable, Model model) {
        Page<UserDtoList> users = userService.getAll(pageable);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", pageable.getPageNumber());
        return "admin-users";
    }
}
