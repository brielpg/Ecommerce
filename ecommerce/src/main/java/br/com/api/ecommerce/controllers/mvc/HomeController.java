package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.CategoryService;
import br.com.api.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        Pageable pageable = PageRequest.of(0, 10); // Página 0, tamanho 10
        Page<ProductDtoList> products = productService.getAll(pageable);
        Page<CategoryDtoList> categories = categoryService.getAll(pageable);

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "home";
    }
}
