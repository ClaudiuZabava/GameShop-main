package com.awbd.gameshop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private int id;

    @Column(name="name")
    @NotNull
    @NotEmpty(message = "The name cannot be blank!")
    private String name;

    @Column(name = "price")
    @Min(value = 0)
    @NotNull(message = "The price must be set!")
    @Positive(message = "Price should be greater than zero")
    private double price;

    @Column(name = "year_date")
    @Min(value = 0)
    private Integer year = 0;

    @Column(name = "volume")
    @Min(value = 0)
    private Integer volume = 0;

    @Column(name = "series_name")
    private String series_name = null;

    @Column(name = "is_deleted")
    private Boolean is_deleted = false;

    @ManyToOne(targetEntity = Author.class)
    @PrimaryKeyJoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(targetEntity = Category.class)
    private List<Category> gameCategories = null;

    @OneToMany(mappedBy = "game")
    private List<GameBasket> gameBaskets = null;

    public Game() {
    }

    public Game(int id, String name, double price, int year, int volume, String series_name, Boolean is_deleted, Author author) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.is_deleted = is_deleted;
        this.author = author;
    }

    public Game(String name, double price, int year, int volume, String series_name, Boolean is_deleted, Author author) {
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.is_deleted = is_deleted;
        this.author = author;
    }

    public Game(int id, String name, double price, int year, int volume, String series_name, Boolean is_deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.is_deleted = is_deleted;
    }

    public Game(int id,
                String name,
                double price,
                int year,
                Boolean is_deleted,
                int volume,
                String series_name,
                Author author) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.is_deleted = is_deleted;
        this.volume = volume;
        this.series_name = series_name;
        this.author = author;
    }

    public Game(int id, String name, double price, int year, int volume, String series_name, Boolean is_deleted, Author author, List<Category> gameCategories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.is_deleted = is_deleted;
        this.author = author;
        this.gameCategories = gameCategories;
    }

    public Game(String name, double price, int year, int volume, String series_name, Boolean is_deleted) {
        this.name = name;
        this.price = price;
        this.year = year;
        this.volume = volume;
        this.series_name = series_name;
        this.is_deleted = is_deleted;
    }

    public Game(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Game(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Category> getGameCategories() {
        return gameCategories;
    }

    public void setGameCategories(List<Category> gameCategories) {
        this.gameCategories = gameCategories;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public List<GameBasket> getGameBaskets() {
        return gameBaskets;
    }

    public void setGameBaskets(List<GameBasket> gameBaskets) {
        this.gameBaskets = gameBaskets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && Double.compare(price, game.price) == 0 && Objects.equals(name, game.name) && Objects.equals(year, game.year) && Objects.equals(volume, game.volume) && Objects.equals(series_name, game.series_name) && Objects.equals(is_deleted, game.is_deleted) && Objects.equals(author, game.author) && Objects.equals(gameCategories, game.gameCategories) && Objects.equals(gameBaskets, game.gameBaskets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, year, volume, series_name, is_deleted, author, gameCategories, gameBaskets);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", year=" + year +
                ", volume=" + volume +
                ", series_name='" + series_name + '\'' +
                ", is_deleted=" + is_deleted +
                ", author=" + author +
                ", gameCategories=" + gameCategories +
                ", gameBaskets=" + gameBaskets +
                '}';
    }
}
