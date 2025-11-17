package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoForm;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Category.CategoryDtoUpdate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.services.ProductService;
import br.com.api.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String admin(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDtoList> users = userService.getAll(pageable);

        model.addAttribute("users", users);
        model.addAttribute("currentPage", users.getNumber());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("categories", categoryService.getAll(PageRequest.of(0, 100)));

        return "admin";
    }

    @GetMapping("/admin/categories")
    public String categories(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDtoList> categories = categoryService.getAll(pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", categories.getNumber());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("categoryDto", new CategoryDtoCreate("", ""));

        return "admin_categories";
    }

    @PostMapping("/admin/categories")
    public String createOrUpdateCategory(@Valid CategoryDtoForm dtoForm, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar categoria: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/categories";
        }
        try {
            if (dtoForm.id() != null) {
                CategoryDtoUpdate dtoUpdate = new CategoryDtoUpdate(dtoForm.id(), dtoForm.name(), dtoForm.description());
                categoryService.update(dtoUpdate);
                redirectAttributes.addFlashAttribute("successMessage", "Categoria atualizada com sucesso!");
            } else {
                CategoryDtoCreate dtoCreate = new CategoryDtoCreate(dtoForm.name(), dtoForm.description());
                categoryService.create(dtoCreate);
                redirectAttributes.addFlashAttribute("successMessage", "Categoria criada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar categoria: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }



    @GetMapping("/admin/products")
    public String products(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) String categoryId,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDtoList> products = productService.getAll(pageable); // TODO: Add search and filter logic

        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("productDto", new ProductDtoCreate("", "", null, null, List.of()));
        model.addAttribute("allCategories", categoryService.getAll(PageRequest.of(0, 100)).getContent());

        return "admin_products";
    }

    @PostMapping("/admin/products")
    public String createProduct(@Valid ProductDtoCreate dto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar produto: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/products";
        }
        try {
            productService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Produto criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar produto: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/users")
    public String users(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDtoList> users = userService.getAll(pageable); // TODO: Add search logic

        model.addAttribute("users", users);
        model.addAttribute("currentPage", users.getNumber());
        model.addAttribute("totalPages", users.getTotalPages());

        return "admin_users";
    }
}
