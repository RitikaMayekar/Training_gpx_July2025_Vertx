package com.globalpayex.domain;

public class Book {
    private long id;
    private String title;
    private int price;
    private int pages;

    public Book(){}

    public Book(long id, String title, int price, int pages) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.pages = pages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
