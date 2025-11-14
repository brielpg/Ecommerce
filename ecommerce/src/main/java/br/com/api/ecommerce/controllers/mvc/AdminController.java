package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
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
        model.addAttribute("categoryDto", new CategoryDtoCreate("", ""));
        model.addAttribute("productDto", new ProductDtoCreate("", "", null, null, List.of()));
        model.addAttribute("categories", categoryService.getAll(PageRequest.of(0, 100)).getContent());

        return "admin";
    }

    @PostMapping("/admin/categories")
    public String createCategory(@Valid CategoryDtoCreate dto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryError", "Erro ao criar categoria: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin";
        }
        try {
            categoryService.create(dto);
            redirectAttributes.addFlashAttribute("categorySuccess", "Categoria criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("categoryError", "Erro ao criar categoria: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/products")
    public String createProduct(@Valid ProductDtoCreate dto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("productError", "Erro ao criar produto: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin";
        }
        try {
            productService.create(dto);
            redirectAttributes.addFlashAttribute("productSuccess", "Produto criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("productError", "Erro ao criar produto: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
