package com.example.bookrent;

public class Book {
    private int id;
    private String title;
    private String image;
    private String description;
    private String author;
    private String reviews;

    public Book(int id, String title, String image, String description, String author, String reviews) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.author = author;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getReviews() {
        return reviews;
    }
}
