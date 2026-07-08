package com.i2i.rdbmsoracle.dto;

public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String publisher;

    public BookDto(Long id, String title, String author, String publisher) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }
}