package com.awbd.gameshop.services.gamebasket;

import com.awbd.gameshop.exceptions.exceptions.NoFoundElementException;
import com.awbd.gameshop.models.Basket;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.GameBasket;
import com.awbd.gameshop.repositories.GameBasketRepository;
import com.awbd.gameshop.services.game.IGameService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class GameBasketService implements IGameBasketService {
    private final GameBasketRepository gameBasketRepository;
    private final IGameService gameService;

    public GameBasketService(GameBasketRepository gameBasketRepository,
                             IGameService gameService) {
        this.gameBasketRepository = gameBasketRepository;
        this.gameService = gameService;
    }

    @Transactional
    @Override
    public Double addGameToBasket(Integer gameId, Basket basket) {
        Game game = gameService.getGameById(gameId);

        GameBasket gameBasket = gameBasketRepository.findGameInBasket(game.getId(), basket.getId()).orElse(null);

        if(gameBasket == null) {
            gameBasket = gameBasketRepository.save(new GameBasket(
                    0,
                    1,
                    game.getPrice(),
                    game,
                    basket
            ));
        } else {
            gameBasket.setCopies(gameBasket.getCopies() + 1);
            gameBasketRepository.save(gameBasket);
        }

        return gameBasket.getPrice();
    }

    @Transactional
    @Override
    public Double removeGameToBasket(int gameId, int basketId) {
        GameBasket gameBasket = gameBasketRepository.findGameInBasket(gameId, basketId).orElseThrow(
                () -> new NoFoundElementException("The game is not in this basket"));

        gameBasketRepository.delete(gameBasket);

        return gameBasket.getPrice() * gameBasket.getCopies();
    }

    @Transactional
    @Override
    public Double decrementGameFromBasket(int gameId, int basketId) {
        GameBasket gameBasket = gameBasketRepository.findGameInBasket(gameId, basketId).orElseThrow(
                () -> new NoFoundElementException("The game is not in this basket"));

        if (gameBasket.getCopies() > 1)
        {
            gameBasket.setCopies(gameBasket.getCopies() - 1);
            gameBasketRepository.save(gameBasket);
        } else {
            gameBasketRepository.delete(gameBasket);
        }

        return gameBasket.getPrice();
    }
}
