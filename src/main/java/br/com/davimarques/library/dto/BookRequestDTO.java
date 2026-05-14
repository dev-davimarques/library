package br.com.davimarques.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookRequestDTO(
        Long id,

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, message = "O título deve ter pelo menos 3 caracteres")
        String title,

        @NotBlank(message = "O ISBN é obrigatório")
        @Pattern(regexp = "[0-9\\-]+", message = "O ISBN deve conter apenas números e hífens.")
        String isbn,

        @NotNull(message = "Selecione uma editora")
        Long publisherId,

        @NotEmpty(message = "Selecione pelo menos um autor")
        List<Long> authorIds
) {}