package com.example.bookshelf.service;
import com.example.bookshelf.dto.AuthorDTO;
import com.example.bookshelf.dto.AuthorWithBooksDTO;
import com.example.bookshelf.dto.BookDTO;
import com.example.bookshelf.entity.Author;
import com.example.bookshelf.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {
    

    private AuthorRepository authorRepository;

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId() );
        dto.setName(author.getName());
        return dto;
    }


@Transactional
public AuthorWithBooksDTO getAuthorWithBooksById(Long id) {
    Author author = authorRepository.findAuthorWithBooksById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    AuthorWithBooksDTO dto = new AuthorWithBooksDTO();
    dto.setId(author.getId());
    dto.setName(author.getName());
    dto.setBooks(author.getBooks().stream().map(book -> {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        return bookDTO;
    }).collect(Collectors.toSet()));
    return dto;
}

//    @Transactional
//    public AuthorWithBooksDTO getAuthorWithBooksById(Long id) {
//        return authorRepository.findAuthorWithBooksById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
//    }

    
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        author.setName(authorDetails.getName());
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
        authorRepository.delete(author);
    }
    // Новый метод для получения всех авторов
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName()))
                .collect(Collectors.toList());
    }
}
