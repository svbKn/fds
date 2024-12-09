package com.example.bookshelf;
import com.example.bookshelf.controller.AuthorController;
import com.example.bookshelf.dto.AuthorDTO;
import com.example.bookshelf.dto.AuthorWithBooksDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.service.AuthorService;
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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthorService authorService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCreateAuthor_Success() throws Exception {
        // Arrange
        Author author = new Author("J.K. Rowling");
        author.setId(1L);
        Mockito.when(authorService.createAuthor(any(Author.class))).thenReturn(author);
        
        // Act & Assert
        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("J.K. Rowling"));
    }
    
    @Test
    void testGetAuthor_Success() throws Exception {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("J.K. Rowling");
        Mockito.when(authorService.getAuthorById(1L)).thenReturn(authorDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/authors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("J.K. Rowling"));
    }
    

    @Test
    void testGetAuthorWithBooks_Success() throws Exception {
        // Arrange
        AuthorWithBooksDTO dto = new AuthorWithBooksDTO();
        dto.setId(1L);
        dto.setName("J.K. Rowling");
        // Assume books are set accordingly
        Mockito.when(authorService.getAuthorWithBooksById(1L)).thenReturn(dto);
        
        // Act & Assert
        mockMvc.perform(get("/api/authors/1/with-books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("J.K. Rowling"));
            // Additional assertions for books can be added
    }
    
    @Test
    void testUpdateAuthor_Success() throws Exception {
        // Arrange
        Author updatedAuthor = new Author("Joanne Rowling");
        updatedAuthor.setId(1L);
        Mockito.when(authorService.updateAuthor(eq(1L), any(Author.class))).thenReturn(updatedAuthor);
        
        // Act & Assert
        mockMvc.perform(put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuthor)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Joanne Rowling"));
    }
    
    @Test
    void testDeleteAuthor_Success() throws Exception {
        // Arrange
        Mockito.doNothing().when(authorService).deleteAuthor(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/authors/1"))
            .andExpect(status().isNoContent());
    }
}
