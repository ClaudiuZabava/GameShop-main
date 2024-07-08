package com.awbd.gameshop.repositories;

import com.awbd.gameshop.models.GameBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameBasketRepository extends JpaRepository<GameBasket, Integer> {
    @Query("SELECT b FROM GameBasket b WHERE b.game.id = :gameId AND b.basket.id = :basketId")
    Optional<GameBasket> findGameInBasket(Integer gameId, Integer basketId);
}
