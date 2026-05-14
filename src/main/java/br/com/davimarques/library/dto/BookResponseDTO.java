package br.com.davimarques.library.dto;

import br.com.davimarques.library.model.Book;

import java.util.List;

// Usado para exibir os dados na tabela
public record BookResponseDTO(
        Long id,
        String title,
        String isbn,
        String publisherName,
        List<String> authorNames
) {
    public static BookResponseDTO fromEntity(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublisher().getName(),
                book.getAuthors().stream().map(a -> a.getName()).toList()
        );
    }
}
