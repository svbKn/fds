package com.example.bookshelf.service;
import com.example.bookshelf.dto.AuthorDTO;
import com.example.bookshelf.dto.BookDTO;
import com.example.bookshelf.dto.BookWithAuthorDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.repository.AuthorRepository;
import com.example.bookshelf.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    

    private BookRepository bookRepository;
    

    private AuthorRepository authorRepository;

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        return dto;
    }

    public BookWithAuthorDTO getBookWithAuthorById(Long id) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        BookWithAuthorDTO dto = new BookWithAuthorDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        Author author = book.getAuthor();
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        dto.setAuthor(authorDTO);
        return dto;
    }


    
    public Book createBook(Book book) {
        Long authorId = book.getAuthor().getId();
        Author author = authorRepository.findById(authorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
       book.setTitle(bookDetails.getTitle());
      //  book.setTitle("ABC");
        if (bookDetails.getAuthor() != null) {
            Long authorId = bookDetails.getAuthor().getId();
            Author author = authorRepository.findById(authorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId));
            book.setAuthor(author);
        }
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        bookRepository.delete(book);
    }
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle()))
                .collect(Collectors.toList());
    }
}
