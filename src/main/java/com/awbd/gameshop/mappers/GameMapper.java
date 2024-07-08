package com.awbd.gameshop.mappers;

import com.awbd.gameshop.dtos.GameResponse;
import com.awbd.gameshop.dtos.RequestGame;
import com.awbd.gameshop.models.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public Game requestGame(RequestGame newGame) {
        return new Game(
            newGame.getName(),
            newGame.getPrice(),
            newGame.getYear(),
            newGame.getVolume(),
            newGame.getSeries_name(),
            false
        );
    }

    public GameResponse gameDto(Game game) {
        return new GameResponse(
            game.getId(),
            game.getName(),
            game.getPrice(),
            game.getYear(),
            game.getVolume(),
            game.getSeries_name(),
            game.getAuthor().getFirstName() + game.getAuthor().getLastName(),
            game.getGameCategories()
        );
    }


}
