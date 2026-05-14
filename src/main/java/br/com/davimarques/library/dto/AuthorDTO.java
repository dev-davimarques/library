package br.com.davimarques.library.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorDTO(
        Long id,
        @NotBlank(message = "O nome do autor é obrigatório")
        String name
) {
    public static AuthorDTO fromEntity(br.com.davimarques.library.model.Author author) {
        return new AuthorDTO(author.getId(), author.getName());
    }
}
