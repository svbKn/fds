package com.example.bookshelf;

import com.example.bookshelf.controller.BookController;
import com.example.bookshelf.dto.BookDTO;
import com.example.bookshelf.dto.BookWithAuthorDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BookService bookService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCreateBook_Success() throws Exception {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book book = new Book("Harry Potter and the Sorcerer's Stone", author);
        book.setId(1L);
        Mockito.when(bookService.createBook(any(Book.class))).thenReturn(book);
        
        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Harry Potter and the Sorcerer's Stone"));
    }
    
    @Test
    void testGetBook_Success() throws Exception {
        // Arrange
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Harry Potter and the Sorcerer's Stone");
        Mockito.when(bookService.getBookById(1L)).thenReturn(bookDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Harry Potter and the Sorcerer's Stone"));
    }

//    @Test
//    void testGetBook_NotFound() throws Exception {
//        // Arrange
//        Mockito.when(bookService.getBookById(1L)).thenThrow(new com.example.bookshelf.service.ResourceNotFoundException("Book not found with id 1"));
//
//        // Act & Assert
//        mockMvc.perform(get("/api/books/1"))
//            .andExpect(status().isNotFound())
//            .andExpect(content().string("Book not found with id 1"));
//    }
    
    @Test
    void testGetBookWithAuthor_Success() throws Exception {
        // Arrange
        BookWithAuthorDTO dto = new BookWithAuthorDTO();
        dto.setId(1L);
        dto.setTitle("Harry Potter and the Sorcerer's Stone");
        // Assume author is set accordingly
        Mockito.when(bookService.getBookWithAuthorById(1L)).thenReturn(dto);
        
        // Act & Assert
        mockMvc.perform(get("/api/books/1/with-author"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Harry Potter and the Sorcerer's Stone"));
            // Additional assertions for author can be added
    }
    
    @Test
    void testUpdateBook_Success() throws Exception {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book updatedBook = new Book("Harry Potter and the Philosopher's Stone", author);
        updatedBook.setId(1L);
        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);
        
        // Act & Assert
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Harry Potter and the Philosopher's Stone"));
    }
    
    @Test
    void testDeleteBook_Success() throws Exception {
        // Arrange
        Mockito.doNothing().when(bookService).deleteBook(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent());
    }
}
