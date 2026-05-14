package br.com.davimarques.library.service;

import br.com.davimarques.library.dto.AuthorDTO;
import br.com.davimarques.library.model.Author;
import br.com.davimarques.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;

    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        // Alterado para ASC: primeiro o ID 1, depois o 2, etc.
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(AuthorDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorDTO findById(Long id) {
        return repository.findById(id)
                .map(AuthorDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado."));
    }

    @Transactional
    public void save(AuthorDTO dto) {
        boolean isNew = dto.id() == null;
        Author author;

        // 1. Instancia ou busca o autor
        if (isNew) {
            author = new Author();
        } else {
            author = repository.findById(dto.id())
                    .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado."));
        }

        // 2. Valida duplicidade (Apenas se for um novo autor OU se o nome estiver sendo alterado)
        if ((isNew || !author.getName().equalsIgnoreCase(dto.name()))
                && repository.existsByNameIgnoreCase(dto.name())) {
            throw new IllegalArgumentException("Já existe um autor cadastrado com este nome.");
        }

        // 3. Atualiza e salva
        author.setName(dto.name());
        repository.save(author);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}