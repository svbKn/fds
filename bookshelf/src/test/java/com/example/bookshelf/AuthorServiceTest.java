package com.example.bookshelf;

import com.example.bookshelf.dto.AuthorDTO;
import com.example.bookshelf.dto.AuthorWithBooksDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.entity.Book;
import com.example.bookshelf.repository.AuthorRepository;
import com.example.bookshelf.service.AuthorService;
import com.example.bookshelf.service.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    
    @InjectMocks
    private AuthorService authorService;

    @Test
    void testGetAuthorById_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        
        // Act
        AuthorDTO dto = authorService.getAuthorById(1L);
        
        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("J.K. Rowling", dto.getName());
        verify(authorRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetAuthorById_NotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getAuthorById(1L);
        });
        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }
    
    @Test
    void testCreateAuthor_Success() {
        // Arrange
        Author author = new Author("George R.R. Martin");
        author.setId(2L);
        when(authorRepository.save(ArgumentMatchers.any(Author.class))).thenReturn(author);
        
        // Act
        Author createdAuthor = authorService.createAuthor(new Author("George R.R. Martin"));
        
        // Assert
        assertNotNull(createdAuthor);
        assertEquals(2L, createdAuthor.getId());
        assertEquals("George R.R. Martin", createdAuthor.getName());
        verify(authorRepository, times(1)).save(ArgumentMatchers.any(Author.class));
    }
    
    @Test
    void testUpdateAuthor_Success() {
        // Arrange
        Author existingAuthor = new Author("J.K. Rowling");
        existingAuthor.setId(1L);
        Author updatedDetails = new Author("Joanne Rowling");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);
        
        // Act
        Author updatedAuthor = authorService.updateAuthor(1L, updatedDetails);
        
        // Assert
        assertNotNull(updatedAuthor);
        assertEquals("Joanne Rowling", updatedAuthor.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(existingAuthor);
    }
    
    @Test
    void testUpdateAuthor_NotFound() {
        // Arrange
        Author updatedDetails = new Author("Joanne Rowling");
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.updateAuthor(1L, updatedDetails);
        });
        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(0)).save(any(Author.class));
    }
    
    @Test
    void testDeleteAuthor_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).delete(author);
        
        // Act
        authorService.deleteAuthor(1L);
        
        // Assert
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).delete(author);
    }
    
    @Test
    void testDeleteAuthor_NotFound() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authorService.deleteAuthor(1L);
        });
        assertEquals("Author not found with id 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(0)).delete(any(Author.class));
    }


    @Test
    void testGetAuthorWithBooksById_Success() {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Set<Book> books = new HashSet<>();
        Book book1 = new Book("Harry Potter and the Sorcerer's Stone", author);
        book1.setId(1L);
        Book book2 = new Book("Harry Potter and the Chamber of Secrets", author);
        book2.setId(2L);
        books.add(book1);
        books.add(book2);
        author.setBooks(books);

        // Mock the correct repository method
        when(authorRepository.findAuthorWithBooksById(1L)).thenReturn(Optional.of(author));

        // Act
        AuthorWithBooksDTO dto = authorService.getAuthorWithBooksById(1L);

        // Assert
        assertNotNull(dto, "The returned DTO should not be null");
        assertEquals(1L, dto.getId(), "Author ID should match");
        assertEquals("J.K. Rowling", dto.getName(), "Author name should match");
        assertNotNull(dto.getBooks(), "Books should not be null");
        assertEquals(2, dto.getBooks().size(), "Author should have 2 books");

        // Verify that findAuthorWithBooksById was called with the correct parameter
        verify(authorRepository, times(1)).findAuthorWithBooksById(1L);
    }


}
