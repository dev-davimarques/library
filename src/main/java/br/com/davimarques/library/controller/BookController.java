package br.com.davimarques.library.controller;

import br.com.davimarques.library.dto.BookRequestDTO;
import br.com.davimarques.library.service.AuthorService;
import br.com.davimarques.library.service.BookService;
import br.com.davimarques.library.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    @GetMapping
    public String dashboard(@RequestParam(required = false) String title,
                            @RequestParam(required = false) Long publisherId,
                            @RequestParam(required = false) Long authorId,
                            Model model) {
        prepareModelData(model);
        model.addAttribute("bookDto", new BookRequestDTO(null, "", "", null, new ArrayList<>()));
        model.addAttribute("books", bookService.search(title, publisherId, authorId));
        return "book";
    }

    // ==========================================
    // NOVAS ROTAS DE EDIÇÃO E EXCLUSÃO
    // ==========================================

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        prepareModelData(model);
        // Busca o livro pelo ID e joga no DTO do formulário
        model.addAttribute("bookDto", bookService.findById(id));
        // Recarrega a tabela de livros
        model.addAttribute("books", bookService.search(null, null, null));
        return "book";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // Chama o service para deletar o livro
        bookService.delete(id);
        // Redireciona de volta para o dashboard
        return "redirect:/books";
    }

    // ==========================================

    @PostMapping
    public String save(@Valid @ModelAttribute("bookDto") BookRequestDTO bookDto,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            prepareModelData(model);
            model.addAttribute("books", bookService.search(null, null, null));
            return "book";
        }

        try {
            bookService.save(bookDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("isbn", "error.bookDto", e.getMessage());
            prepareModelData(model);
            model.addAttribute("books", bookService.search(null, null, null));
            return "book";
        }

        return "redirect:/books";
    }

    private void prepareModelData(Model model) {
        model.addAttribute("allPublishers", publisherService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());
    }
}