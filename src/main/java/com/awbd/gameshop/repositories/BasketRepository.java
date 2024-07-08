package com.awbd.gameshop.repositories;

import com.awbd.gameshop.dtos.GameFromBasketDetails;
import com.awbd.gameshop.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {
    @Query("SELECT b FROM Basket b WHERE b.user.id = :userId AND b.sent = false")
    Optional<Basket> findByUserId(Integer userId);

    @Query("SELECT new com.awbd.gameshop.dtos.GameFromBasketDetails(gameBasket.game.name, gameBasket.price, gameBasket.copies, gameBasket.game.id) " +
            "FROM GameBasket gameBasket " +
            "WHERE gameBasket.basket.id = :basketId")
    List<GameFromBasketDetails> findGamesFromCurrentBasket(Integer basketId);
}
