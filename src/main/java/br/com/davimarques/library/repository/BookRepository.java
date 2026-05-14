package br.com.davimarques.library.repository;

import br.com.davimarques.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.publisher LEFT JOIN FETCH b.authors a " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:publisherId IS NULL OR b.publisher.id = :publisherId) " +
            "AND (:authorId IS NULL OR a.id = :authorId)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("publisherId") Long publisherId,
                           @Param("authorId") Long authorId);
}
