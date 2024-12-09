package com.example.bookshelf.repository;

import com.example.bookshelf.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {


    @EntityGraph(attributePaths = {"books"})
    Optional<Author> findAuthorWithBooksById(Long id);


//    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id = :id")
//    Optional<AuthorWithBooksDTO> findAuthorWithBooksById(Long id);

}
