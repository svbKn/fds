package com.example.bookshelf.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookWithAuthorDTO {
    private Long id;
    private String title;
    private AuthorDTO author;


}
