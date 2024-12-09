package com.example.bookshelf.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // Many Books can have one Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;




    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }


}
