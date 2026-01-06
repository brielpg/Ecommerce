package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductMvcController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam(value = "q", required = false) String query,
                                 Pageable pageable,
                                 Model model) {

        Page<ProductDtoList> products = productService.search(query, pageable);

        model.addAttribute("products", products);
        model.addAttribute("query", query); // Para manter o termo no input de busca

        return "product-search"; // Nome da nova página HTML
    }
}
