package com.example.hpnotebook.booklister;

/**
 * Created by Hp Notebook on 21-01-2018.
 */

public class Book {
    private String img;
    private String name;
    private String author;
    private double ratings;
    private double price;
    private String url;

    public Book(String img, String name, String author, double ratings, double price, String url) {
        this.img = img;
        this.name = name;
        this.author = author;
        this.ratings = ratings;
        this.price = price;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public double getRatings() {
        return ratings;
    }

    public double getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }
}