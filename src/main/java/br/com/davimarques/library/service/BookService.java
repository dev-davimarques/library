package br.com.davimarques.library.service;

import br.com.davimarques.library.dto.BookRequestDTO;
import br.com.davimarques.library.dto.BookResponseDTO;
import br.com.davimarques.library.model.Author;
import br.com.davimarques.library.model.Book;
import br.com.davimarques.library.model.Publisher;
import br.com.davimarques.library.repository.AuthorRepository;
import br.com.davimarques.library.repository.BookRepository;
import br.com.davimarques.library.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<BookResponseDTO> search(String title, Long publisherId, Long authorId) {
        String searchTitle = (title != null && title.isBlank()) ? null : title;
        return bookRepository.searchBooks(searchTitle, publisherId, authorId)
                .stream()
                .map(BookResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookRequestDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        List<Long> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .toList();

        return new BookRequestDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublisher().getId(),
                authorIds
        );
    }

    @Transactional
    public void save(BookRequestDTO dto) {
        // 1. Formata o ISBN vindo do usuário (remove sujeira e aplica máscara)
        String formattedIsbn = formatIsbn(dto.isbn());

        boolean isNew = dto.id() == null;
        Book book;

        // 2. Busca ou Instancia a entidade
        if (isNew) {
            book = new Book();
        } else {
            book = bookRepository.findById(dto.id())
                    .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));
        }

        // 3. Valida duplicidade de ISBN (Se for novo ou se o ISBN mudou)
        if ((isNew || !book.getIsbn().equals(formattedIsbn)) && bookRepository.existsByIsbn(formattedIsbn)) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com este ISBN.");
        }

        // 4. Carrega dependências
        Publisher publisher = publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada."));
        List<Author> authors = authorRepository.findAllById(dto.authorIds());

        // 5. Popula a entidade e salva
        book.setTitle(dto.title());
        book.setIsbn(formattedIsbn);
        book.setPublisher(publisher);
        book.setAuthors(authors);

        bookRepository.save(book);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Limpa caracteres não numéricos e aplica a máscara 000-00-000-0000-0
     * Suporta ISBN-13 (13 dígitos)
     */
    private String formatIsbn(String isbn) {
        if (isbn == null) return "";

        // Remove tudo que não é número
        String numbers = isbn.replaceAll("\\D", "");

        // Aplica a máscara apenas se tiver os 13 dígitos
        if (numbers.length() == 13) {
            return numbers.replaceFirst("(\\d{3})(\\d{2})(\\d{3})(\\d{4})(\\d{1})", "$1-$2-$3-$4-$5");
        }

        // Caso contrário, retorna apenas os números (ou trate como erro se preferir)
        return numbers;
    }
}