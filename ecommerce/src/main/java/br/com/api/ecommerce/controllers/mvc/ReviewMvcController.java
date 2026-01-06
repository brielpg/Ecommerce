package br.com.api.ecommerce.controllers.mvc;

import br.com.api.ecommerce.models.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewMvcController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/save")
    public String saveReview(@ModelAttribute ReviewDtoCreate dto, RedirectAttributes redirectAttributes) {
        try {
            reviewService.create(dto);
            redirectAttributes.addFlashAttribute("success", "Avaliação publicada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro: Certifique-se de que o produto foi entregue.");
        }
        return "redirect:/products/" + dto.productId();
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable UUID id, @RequestParam UUID productId) {
        reviewService.delete(id);
        return "redirect:/products/" + productId;
    }
}