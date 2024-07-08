package com.awbd.gameshop.repositories;

import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
class GameRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAvailableGames() {
        // Mock author
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        // Mock game
        Game game1 = new Game("game1", 12);
        game1.setAuthor(author);

        Game game2 = new Game("game2", 20);
        game2.setAuthor(author);

        // Mock
        when(gameRepository.getAvailableGames(any())).thenReturn(new PageImpl<>(List.of(game1, game2)));

        // Test
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Game> page = gameRepository.getAvailableGames(pageable);

        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(game1));
        assertTrue(page.getContent().contains(game2));

        // Verify
        verify(gameRepository).getAvailableGames(any());
    }

    @Test
    void getAvailableGames_notFound() {
        when(gameRepository.getAvailableGames(any())).thenReturn(Page.empty());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Game> page = gameRepository.getAvailableGames(pageable);

        assertEquals(0, page.getTotalElements());

        verify(gameRepository).getAvailableGames(any());
    }

    @Test
    void getGamesByCategory() {
        Category category = new Category("Fiction");
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        Game game = new Game("game1", 12);
        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameRepository.getGamesByCategory(category.getName())).thenReturn(games);

        List<Game> result = gameRepository.getGamesByCategory(category.getName());
        assertEquals(1, result.size());
        assertTrue(result.contains(game));

        verify(gameRepository).getGamesByCategory(category.getName());
    }

    @Test
    void getGamesByCategory_notFound() {
        Category category = new Category("Fiction");
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.empty());

        List<Game> result = gameRepository.getGamesByCategory(category.getName());
        assertEquals(0, result.size());

        verify(gameRepository, times(1)).getGamesByCategory(category.getName());
    }

    @Test
    void getGamesByAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        Game game1 = new Game("game1", 12);
        Game game2 = new Game("game2", 12);
        List<Game> games = List.of(game1, game2);

        when(gameRepository.getGamesByAuthor(author.getFirstName(), author.getLastName())).thenReturn(games);

        List<Game> result = gameRepository.getGamesByAuthor(author.getFirstName(), author.getLastName());
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));

        verify(gameRepository).getGamesByAuthor(author.getFirstName(), author.getLastName());
    }

    @Test
    void getGamesByAuthor_notFound() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");

        List<Game> result = gameRepository.getGamesByAuthor(author.getFirstName(), author.getLastName());
        assertEquals(0, result.size());

        verify(gameRepository).getGamesByAuthor(author.getFirstName(), author.getLastName());
    }

    @Test
    void save() {
        Game game = new Game(1, "game", 12);
        when(gameRepository.save(game)).thenReturn(game);

        Game savedGame = gameRepository.save(game);
        assertNotNull(savedGame);
        assertEquals(game, savedGame);

        verify(gameRepository).save(game);
    }

    @Test
    void findAll() {
        Game game1 = new Game("game1", 12);
        Game game2 = new Game("game2", 12);
        List<Game> games = List.of(game1, game2);

        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameRepository.findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));

        verify(gameRepository).findAll();
    }

    @Test
    void findById() {
        Game game = new Game(1, "game", 12);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Optional<Game> result = gameRepository.findById(game.getId());
        assertTrue(result.isPresent());
        assertEquals(game, result.get());

        verify(gameRepository).findById(game.getId());
    }

    @Test
    void findById_notFound() {
        int nonExistentGameId = 999;
        when(gameRepository.findById(nonExistentGameId)).thenReturn(Optional.empty());

        Optional<Game> result = gameRepository.findById(nonExistentGameId);
        assertTrue(result.isEmpty());

        verify(gameRepository).findById(nonExistentGameId);
    }

    @Test
    void updateGame() {
        Game game = new Game(1, "game", 12);
        when(gameRepository.save(game)).thenReturn(game);

        Game updatedGame = new Game(1, "updated game", 30);
        when(gameRepository.save(updatedGame)).thenReturn(updatedGame);

        Game savedGame = gameRepository.save(game);
        assertNotNull(savedGame);

        savedGame.setName("updated game");
        savedGame.setPrice(30);
        Game updatedSavedGame = gameRepository.save(savedGame);
        assertNotNull(updatedSavedGame);
        assertEquals(savedGame, updatedSavedGame);

        verify(gameRepository, times(2)).save(any());
    }
}
