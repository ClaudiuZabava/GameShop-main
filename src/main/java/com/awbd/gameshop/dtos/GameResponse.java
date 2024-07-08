package com.awbd.gameshop.dtos;

import com.awbd.gameshop.models.Category;

import java.util.List;

public class GameResponse {
    private int id;

    private String name;

    private double price;

    private int year;

    private int volume;

    private String series_name;

    private String author;

    private List<Category> categories;

    public GameResponse(int id, String name, double price, int year, int volume, String series_name, String author, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.author = author;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public int getVolume() {
        return volume;
    }

    public String getSeries_name() {
        return series_name;
    }

    public String getAuthor() {
        return author;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
