package com.awbd.gameshop.services.game;

import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.Category;

import java.util.List;

public interface IGameService {
    Game addGame(Game newGame);

    Game addAuthorToGame(Integer gameId, Author author);

    Game addCategoriesToGame(Integer gameId, List<Category> categories);

    Game updateGame(Game gameToUpdate, Integer id);

    void deleteGame(Integer id);

    List<Game> getGames();

    List<Game> getAvailableGames(Integer pageNo, Integer pageSize);

    List<Game> getGamesByAuthor(String firstName, String lastName);

    List<Game> getGamesByCategory(String category);

    Game getGameById(Integer gameId);
    int numberAvailableGames();
}
