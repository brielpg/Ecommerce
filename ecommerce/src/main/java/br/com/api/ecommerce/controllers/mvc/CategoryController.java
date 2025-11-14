package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Category.CategoryDtoList;
import br.com.api.ecommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("categoryMvcController")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDtoList> categories = categoryService.getAll(pageable);

        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", categories.getNumber());
        model.addAttribute("totalPages", categories.getTotalPages());

        return "categories";
    }
}
