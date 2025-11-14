package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("productMvcController")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDtoList> products = productService.getAll(pageable);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());

        return "products";
    }
}
