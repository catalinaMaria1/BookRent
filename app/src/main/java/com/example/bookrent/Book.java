package com.example.bookrent;

public class Book {
    private int id;
    private String title;
    private String image;
    private String description;
    private String author;
    private String reviews;
    private double price;

    // Constructor cu toți parametrii
    public Book(int id, String title, String image, String description, String author, String reviews, double price) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.author = author;
        this.reviews = reviews;
        this.price = price;
    }

    // Constructor fără preț
    public Book(int id, String title, String image, String description, String author, String reviews) {
        this(id, title, image, description, author, reviews, 0.0); // preț implicit 0.0
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
