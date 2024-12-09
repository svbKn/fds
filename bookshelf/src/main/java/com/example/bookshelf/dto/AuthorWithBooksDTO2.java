package com.example.bookshelf.dto;

import java.util.Set;

public interface AuthorWithBooksDTO2 {
    Long getId();
    String getName();
    Set<BookDTO> getBooks();

    interface BookDTO {
        Long getId();
        String getTitle();
    }
}
