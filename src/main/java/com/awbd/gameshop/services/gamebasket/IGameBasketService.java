package com.awbd.gameshop.services.gamebasket;

import com.awbd.gameshop.models.Basket;

public interface IGameBasketService {
    Double addGameToBasket(Integer gameId, Basket basket);

    Double removeGameToBasket(int gameId, int basketId);

    Double decrementGameFromBasket(int gameId, int basketId);
}
