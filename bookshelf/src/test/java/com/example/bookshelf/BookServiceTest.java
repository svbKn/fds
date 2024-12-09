package com.example.bookshelf;

import com.example.bookshelf.dto.BookDTO;
import com.example.bookshelf.dto.BookWithAuthorDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.repository.AuthorRepository;
import com.example.bookshelf.repository.BookRepository;
import com.example.bookshelf.service.BookService;
import com.example.bookshelf.service.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private AuthorRepository authorRepository;
    
    @InjectMocks
    private BookService bookService;
    
    @Test
    void testGetBookById_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book book = new Book("Harry Potter and the Sorcerer's Stone", author);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        
        // Act
        BookDTO dto = bookService.getBookById(1L);
        
        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Harry Potter and the Sorcerer's Stone", dto.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetBookById_NotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }
    
    @Test
    void testCreateBook_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book bookToCreate = new Book("Harry Potter and the Chamber of Secrets", author);
        Book createdBook = new Book("Harry Potter and the Chamber of Secrets", author);
        createdBook.setId(2L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(createdBook);
        
        // Act
        Book result = bookService.createBook(bookToCreate);
        
        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Harry Potter and the Chamber of Secrets", result.getTitle());
        assertEquals(author, result.getAuthor());
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(bookToCreate);
    }
    
    @Test
    void testCreateBook_AuthorNotFound() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book bookToCreate = new Book("Harry Potter and the Prisoner of Azkaban", author);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.createBook(bookToCreate);
        });
        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).save(any(Book.class));
    }
    
    @Test
    void testUpdateBook_Success() {
        // Arrange
        Author existingAuthor = new Author("J.K. Rowling");
        existingAuthor.setId(1L);
        Book existingBook = new Book("Harry Potter and the Sorcerer's Stone", existingAuthor);
        existingBook.setId(1L);
        
        Author newAuthor = new Author("John Doe");
        newAuthor.setId(2L);
        Book updatedDetails = new Book("Harry Potter and the Philosopher's Stone", newAuthor);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        
        // Act
        Book updatedBook = bookService.updateBook(1L, updatedDetails);
        
        // Assert
        assertNotNull(updatedBook);
        assertEquals("Harry Potter and the Philosopher's Stone", updatedBook.getTitle());
        assertEquals(newAuthor, updatedBook.getAuthor());
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(2L);
        verify(bookRepository, times(1)).save(existingBook);
    }
    
    @Test
    void testUpdateBook_BookNotFound() {
        // Arrange
        Book updatedDetails = new Book("Some Book Title", null);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, updatedDetails);
        });
        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).save(any(Book.class));
    }
    
    @Test
    void testUpdateBook_AuthorNotFound() {
        // Arrange
        Author existingAuthor = new Author("J.K. Rowling");
        existingAuthor.setId(1L);
        Book existingBook = new Book("Harry Potter and the Sorcerer's Stone", existingAuthor);
        existingBook.setId(1L);
        
        Author newAuthor = new Author("John Doe");
        newAuthor.setId(2L);
        Book updatedDetails = new Book("Harry Potter and the Philosopher's Stone", newAuthor);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, updatedDetails);
        });
        assertEquals("Author not found with id 2", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(2L);
        verify(bookRepository, times(0)).save(any(Book.class));
    }
    
    @Test
    void testDeleteBook_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book book = new Book("Harry Potter and the Sorcerer's Stone", author);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        
        // Act
        bookService.deleteBook(1L);
        
        // Assert
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(book);
    }
    
    @Test
    void testDeleteBook_NotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).delete(any(Book.class));
    }
    
    @Test
    void testGetBookWithAuthorById_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Book book = new Book("Harry Potter and the Sorcerer's Stone", author);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        
        // Act
        BookWithAuthorDTO dto = bookService.getBookWithAuthorById(1L);
        
        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Harry Potter and the Sorcerer's Stone", dto.getTitle());
        assertNotNull(dto.getAuthor());
        assertEquals(1L, dto.getAuthor().getId());
        assertEquals("J.K. Rowling", dto.getAuthor().getName());
        verify(bookRepository, times(1)).findById(1L);
    }
}
