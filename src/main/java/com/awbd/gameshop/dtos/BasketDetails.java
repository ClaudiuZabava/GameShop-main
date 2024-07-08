package com.awbd.gameshop.dtos;


import java.util.List;
import java.util.Objects;

public class BasketDetails {
    private int id;
    private String sent;
    private int userId;
    private String email;
    private double cost;
    private List<GameFromBasketDetails> games;

    public BasketDetails(int id, String sent, int userId, String email, double cost, List<GameFromBasketDetails> games) {
        this.id = id;
        this.sent = sent;
        this.userId = userId;
        this.email = email;
        this.cost = cost;
        this.games = games;
    }

    public int getId() {
        return id;
    }

    public String getSent() {
        return sent;
    }

    public String getEmail() {
        return email;
    }

    public double getCost() {
        return cost;
    }

    public List<GameFromBasketDetails> getGames() {
        return games;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketDetails that = (BasketDetails) o;
        return id == that.id && userId == that.userId && cost == that.cost && Objects.equals(sent, that.sent) && Objects.equals(email, that.email) && Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sent, userId, email, cost, games);
    }

    @Override
    public String toString() {
        return "BasketDetails{" +
                "id=" + id +
                ", sent='" + sent + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", cost=" + cost +
                ", games=" + games +
                '}';
    }
}