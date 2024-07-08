package com.awbd.gameshop.repositories;

import com.awbd.gameshop.models.Game;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
class GameRepositoryH2Test {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void getAvailableGames() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Game> page = gameRepository.getAvailableGames(pageable);
        assertNotNull(page.getContent());

        log.info("getAvailableGames...");
        for (Game game: page.getContent()) {
            log.info("Game id: " + game.getId());
            log.info("Game name: " + game.getName());
            log.info("Game total price: " + game.getPrice());
        }
    }

    @Test
    void getGamesByCategory() {
        List<Game> games = gameRepository.getGamesByCategory("romantic");
        assertNotNull(games);

        log.info("getGamesByCategory - romantic: ");
        for (Game game: games) {
            log.info("Game id: " + game.getId());
            log.info("Game name: " + game.getName());
            log.info("Game total price: " + game.getPrice());
        }
    }

    @Test
    void getGamesByCategory_notFound() {
        List<Game> games = gameRepository.getGamesByCategory("fiction");
        assertEquals(0, games.size());

        log.info("getGamesByCategory_notFound - fiction ...");
        log.info("Games not found: " + games.isEmpty());
    }

    @Test
    void getGamesByAuthor() {
        List<Game> games = gameRepository.getGamesByAuthor("author1_firstname", "author1_lastname");
        assertNotNull(games);

        log.info("getGamesByAuthor...");
        for (Game game: games) {
            log.info("Game id: " + game.getId());
            log.info("Game name: " + game.getName());
            log.info("Game total price: " + game.getPrice());
        }
    }

    @Test
    void getGamesByAuthor_notFound() {
        List<Game> games = gameRepository.getGamesByAuthor("non-existing firstname", "non-existing lastname");
        assertEquals(0, games.size());

        log.info("getGamesByAuthor_notFound...");
        log.info("Games not found: " + games.isEmpty());
    }

    @Test
    void findAll() {
        List<Game> games = gameRepository.findAll();

        log.info("getGamesByAuthor...");
        for (Game game: games) {
            log.info("Game id: " + game.getId());
            log.info("Game name: " + game.getName());
            log.info("Game total price: " + game.getPrice());
        }
    }

    @Test
    void findById() {
        Optional<Game> game = gameRepository.findById(1);
        assertTrue(game.isPresent());

        log.info("findById...");
        log.info("Game id: " + game.get().getId());
        log.info("Game name: " + game.get().getName());
        log.info("Game total price: " + game.get().getPrice());
    }

    @Test
    void findById_notFound() {
        int nonExistentGameId = 999;
        Optional<Game> game = gameRepository.findById(nonExistentGameId);
        assertTrue(game.isEmpty());

        log.info("findById_notFound...");
        log.info("Game found: " + game.isPresent());
    }

    @Test
    void updateGame() {
        Optional<Game> game = gameRepository.findById(1);
        game.get().setName("updated_name");

        Game updatedSavedGame = gameRepository.save(game.get());
        assertNotNull(updatedSavedGame);
        assertEquals("updated_name", updatedSavedGame.getName());

        log.info("updateGame...");
        log.info("Game found: " + game.isPresent());
        log.info("Game name: " + game.get().getName());
    }
}
