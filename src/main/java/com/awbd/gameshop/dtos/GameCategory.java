package com.awbd.gameshop.dtos;

import com.awbd.gameshop.models.Category;

import java.util.List;

public class GameCategory {
    private int id;
    private List<Category> categories;

    public GameCategory(int id, List<Category> categories) {
        this.id = id;
        this.categories = categories;
    }

    public GameCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
