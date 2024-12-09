package com.example.bookshelf.controller;
import com.example.bookshelf.dto.AuthorDTO;
import com.example.bookshelf.dto.AuthorWithBooksDTO;
import com.example.bookshelf.dto.AuthorWithBooksDTO2;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorController {
    

    private AuthorService authorService;


    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author){
        Author createdAuthor = authorService.createAuthor(author);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long id){
        AuthorDTO authorDTO = authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDTO);
    }

//    @GetMapping("/{id}/with-books")
//    public ResponseEntity<AuthorWithBooksDTO2> getAuthorWithBooks(@PathVariable Long id){
//        AuthorWithBooksDTO2 dto = authorService.getAuthorWithBooksById(id);
//        return ResponseEntity.ok(dto);
//    }

    @GetMapping("/{id}/with-books")
    public ResponseEntity<AuthorWithBooksDTO> getAuthorWithBooks(@PathVariable Long id){
        AuthorWithBooksDTO dto = authorService.getAuthorWithBooksById(id );
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails){
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return ResponseEntity.ok(updatedAuthor);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

}
