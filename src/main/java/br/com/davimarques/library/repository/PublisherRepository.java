package br.com.davimarques.library.repository;

import br.com.davimarques.library.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByNameIgnoreCase(String name);
}
