package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductMvcController {

    @Autowired
    private ProductService productService;

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
    public String getProductDetails(@PathVariable UUID id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "product-details";
    }
}
