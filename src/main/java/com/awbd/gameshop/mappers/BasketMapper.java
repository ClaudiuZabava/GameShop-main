package com.awbd.gameshop.mappers;

import com.awbd.gameshop.dtos.BasketDetails;
import com.awbd.gameshop.dtos.GameFromBasketDetails;
import com.awbd.gameshop.models.Basket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketMapper {
    public BasketDetails gameResponse(Basket basket, List<GameFromBasketDetails> games) {
        return new BasketDetails(
                basket.getId(),
                basket.getSent().toString(),
                basket.getUser().getId(),
                basket.getUser().getEmail(),
                basket.getCost(),
                games
        );
    }
}
