package br.com.davimarques.library.service;

import br.com.davimarques.library.dto.PublisherDTO;
import br.com.davimarques.library.model.Publisher;
import br.com.davimarques.library.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository repository;

    @Transactional(readOnly = true)
    public List<PublisherDTO> findAll() {
        // Aqui está a correção: Ordenação Crescente (ASC) pelo ID (1, 2, 3...)
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(PublisherDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PublisherDTO findById(Long id) {
        return repository.findById(id)
                .map(PublisherDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada."));
    }

    @Transactional
    public void save(PublisherDTO dto) {
        boolean isNew = dto.id() == null;
        Publisher publisher;

        // 1. Instancia ou busca a editora
        if (isNew) {
            publisher = new Publisher();
        } else {
            publisher = repository.findById(dto.id())
                    .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada."));
        }

        // 2. Valida duplicidade
        if ((isNew || !publisher.getName().equalsIgnoreCase(dto.name()))
                && repository.existsByNameIgnoreCase(dto.name())) {
            throw new IllegalArgumentException("Já existe uma editora cadastrada com este nome.");
        }

        // 3. Atualiza e salva
        publisher.setName(dto.name());
        repository.save(publisher);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}