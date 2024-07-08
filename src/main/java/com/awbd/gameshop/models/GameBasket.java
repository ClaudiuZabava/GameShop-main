package com.awbd.gameshop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "game_baskets")
public class GameBasket {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "copies")
    @Min(value = 1)
    private int copies = 1;

    @Column(name = "price")
    private double price = 0;

    @ManyToOne(targetEntity = Game.class)
    @PrimaryKeyJoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(targetEntity = Basket.class)
    @PrimaryKeyJoinColumn(name = "basket_id")
    private Basket basket;

    public GameBasket() {
    }

    public GameBasket(int id, int copies, double price, Game game, Basket basket) {
        this.id = id;
        this.copies = copies;
        this.price = price;
        this.game = game;
        this.basket = basket;
    }

    public GameBasket(int copies, double price, Game game, Basket basket) {
        this.copies = copies;
        this.price = price;
        this.game = game;
        this.basket = basket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
