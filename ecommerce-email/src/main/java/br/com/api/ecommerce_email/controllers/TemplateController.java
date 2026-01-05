package br.com.api.ecommerce_email.controllers;

import br.com.api.ecommerce_email.models.EmailTemplate;
import br.com.api.ecommerce_email.repositories.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TemplateController {

    @Autowired
    private EmailTemplateRepository templateRepository;

    @GetMapping("/templates")
    public String listTemplates(Model model) {
        model.addAttribute("templates", templateRepository.findAll());
        model.addAttribute("selectedTemplate", new EmailTemplate());
        return "templates";
    }

    @GetMapping("/templates/edit")
    public String editTemplate(@RequestParam String id, Model model) {
        EmailTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        model.addAttribute("templates", templateRepository.findAll());
        model.addAttribute("selectedTemplate", template);
        return "templates";
    }

    @PostMapping("/templates/update")
    public String updateTemplate(@RequestParam String id, @RequestParam String subject, @RequestParam String htmlContent) {
        EmailTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        template.setSubject(subject);
        template.setHtmlContent(htmlContent);

        templateRepository.save(template);
        return "redirect:/templates";
    }
}
