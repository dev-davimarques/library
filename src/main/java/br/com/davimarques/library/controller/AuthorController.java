// AuthorController.java
package br.com.davimarques.library.controller;

import br.com.davimarques.library.dto.AuthorDTO;
import br.com.davimarques.library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @GetMapping
    public String listAndForm(Model model) {
        model.addAttribute("authorDto", new AuthorDTO(null, ""));
        model.addAttribute("authors", service.findAll());
        return "author";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("authorDto", service.findById(id));
        model.addAttribute("authors", service.findAll());
        return "author";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            service.delete(id);
        } catch (Exception e) {
            // Idealmente tratar DataIntegrityViolationException caso o autor tenha livros
            // Pode redirecionar com uma flag de erro na URL: redirect:/authors?error=in-use
        }
        return "redirect:/authors";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("authorDto") AuthorDTO authorDto,
                       BindingResult result, Model model) {
        // Verifica as anotações do DTO (ex: @NotBlank)
        if (result.hasErrors()) {
            model.addAttribute("authors", service.findAll());
            return "author";
        }

        try {
            service.save(authorDto);
        } catch (IllegalArgumentException e) {
            // Intercepta a exceção de duplicidade e atrela a mensagem ao campo "name"
            result.rejectValue("name", "error.authorDto", e.getMessage());
            model.addAttribute("authors", service.findAll());
            return "author";
        }

        return "redirect:/authors";
    }
}
