package com.example.bookshelf.controller;
import com.example.bookshelf.dto.BookDTO;
import com.example.bookshelf.dto.BookWithAuthorDTO;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {


    private BookService bookService;


    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id){
        BookDTO bookDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }


    @GetMapping("/{id}/with-author")
    public ResponseEntity<BookWithAuthorDTO> getBookWithAuthor(@PathVariable Long id){
        BookWithAuthorDTO dto = bookService.getBookWithAuthorById(id);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody Book bookDetails){
        Book updatedBook = bookService.updateBook(id, bookDetails);
        BookDTO bookDTO = new BookDTO(updatedBook.getId(), updatedBook.getTitle());
        return ResponseEntity.ok(bookDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
}
