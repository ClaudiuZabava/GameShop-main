package com.awbd.gameshop.services.basket;

import com.awbd.gameshop.dtos.GameFromBasketDetails;
import com.awbd.gameshop.models.Basket;

import java.util.List;

public interface IBasketService {
    Basket createBasket(int userId);

    List<GameFromBasketDetails> findGamesFromCurrentBasket(int basketId);

    Basket sentOrder(int userId);

    Basket getBasket(int userId);

    Basket addGameToBasket(int gameId, int basketId);

    Basket removeGameFromBasket(int gameId, int basketId);

    Basket decrementGameFromBasket(int gameId, int basketId);
}
