package com.awbd.gameshop.services.game;

import com.awbd.gameshop.exceptions.exceptions.DeletedGameException;
import com.awbd.gameshop.exceptions.exceptions.NoFoundElementException;
import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.Category;
import com.awbd.gameshop.repositories.GameRepository;
import com.awbd.gameshop.services.author.IAuthorService;
import com.awbd.gameshop.services.category.ICategoryService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService implements IGameService {
    private final GameRepository gameRepository;
    private final ICategoryService categoryService;
    private final IAuthorService authorService;

    public GameService(GameRepository gameRepository, ICategoryService categoryService, IAuthorService authorService) {
        this.gameRepository = gameRepository;
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    @Transactional
    @Override
    public Game addGame(Game newGame) {
        return gameRepository.save(newGame);
    }

    @Transactional
    @Override
    public Game addAuthorToGame(Integer gameId, Author newAuthor) {
        Game game = getGameById(gameId);

        if (game.getIs_deleted())
            throw new DeletedGameException("Cannot add author to a deleted game");

        Author author = authorService.save(newAuthor);
        game.setAuthor(author);

        return gameRepository.save(game);
    }

    @Transactional
    @Override
    public Game addCategoriesToGame(Integer gameId, List<Category> newCategories) {
        Game game = getGameById(gameId);

        if (game.getIs_deleted())
            throw new DeletedGameException("Cannot add categories to a deleted game");

        Set<Category> categories = new HashSet<>(game.getGameCategories());
        for (Category category: newCategories) {
            Category addedCategory = categoryService.save(category);
            categories.add(addedCategory);
        }

        game.setGameCategories(new ArrayList<>(categories));

        return gameRepository.save(game);
    }

    @Transactional
    @Override
    public Game updateGame(Game gameToUpdate, Integer id) {
        Game game = getGameById(id);

        if (game.getIs_deleted())
            throw new DeletedGameException("Cannot update a deleted game");

        game.setName(gameToUpdate.getName());
        game.setSeries_name(gameToUpdate.getSeries_name());
        game.setPrice(gameToUpdate.getPrice());
        game.setVolume(gameToUpdate.getVolume());
        game.setYear(gameToUpdate.getYear());

        return gameRepository.save(game);
    }

    @Transactional
    @Override
    public void deleteGame(Integer id) {
        Game game = getGameById(id);
        game.setIs_deleted(true);

        gameRepository.save(game);
    }

    @Transactional
    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public List<Game> getAvailableGames(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("name").ascending());
        Page<Game> pagedResult =  gameRepository.getAvailableGames(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Game>();
        }
    }


    @Override
    public List<Game> getGamesByAuthor(String firstName, String lastName) {
        return gameRepository.getGamesByAuthor(firstName, lastName);
    }

    @Override
    public List<Game> getGamesByCategory(String category) {
        return gameRepository.getGamesByCategory(category);
    }

    @Override
    public Game getGameById(Integer gameId) {
        return gameRepository.findById(gameId).orElseThrow(
                () -> new NoFoundElementException("Game with this id not found")
        );
    }

    @Override
    public int numberAvailableGames() {
        return gameRepository.getAllAvailableGames().size();
    }
}