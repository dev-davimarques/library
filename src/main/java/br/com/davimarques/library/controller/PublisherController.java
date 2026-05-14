// PublisherController.java
package br.com.davimarques.library.controller;

import br.com.davimarques.library.dto.PublisherDTO;
import br.com.davimarques.library.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService service;

    @GetMapping
    public String listAndForm(Model model) {
        model.addAttribute("publisherDto", new PublisherDTO(null, ""));
        model.addAttribute("publishers", service.findAll());
        return "publisher";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("publisherDto", service.findById(id));
        model.addAttribute("publishers", service.findAll());
        return "publisher";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            service.delete(id);
        } catch (Exception e) {
            // Tratamento de violação de FK
        }
        return "redirect:/publishers";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("publisherDto") PublisherDTO publisherDto,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("publishers", service.findAll());
            return "publisher";
        }

        try {
            service.save(publisherDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "error.publisherDto", e.getMessage());
            model.addAttribute("publishers", service.findAll());
            return "publisher";
        }

        return "redirect:/publishers";
    }
}