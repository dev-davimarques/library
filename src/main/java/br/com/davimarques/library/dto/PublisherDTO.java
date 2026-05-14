package br.com.davimarques.library.dto;

import jakarta.validation.constraints.NotBlank;

public record PublisherDTO(
        Long id,
        @NotBlank(message = "O nome da editora é obrigatório")
        String name
) {
    // Método utilitário para converter Entidade em DTO
    public static PublisherDTO fromEntity(br.com.davimarques.library.model.Publisher publisher) {
        return new PublisherDTO(publisher.getId(), publisher.getName());
    }
}