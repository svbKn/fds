package com.example.bookshelf.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithBooksDTO {
    private Long id;
    private String name;
    private Set<BookDTO> books;


}
