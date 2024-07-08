package com.awbd.gameshop.services.game;

import com.awbd.gameshop.exceptions.exceptions.DatabaseError;
import com.awbd.gameshop.exceptions.exceptions.DeletedGameException;
import com.awbd.gameshop.exceptions.exceptions.NoFoundElementException;
import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.Category;
import com.awbd.gameshop.repositories.GameRepository;
import com.awbd.gameshop.services.author.IAuthorService;
import com.awbd.gameshop.services.category.ICategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    private static final Integer BOOK_ID = 0;
    private static  final Game BOOK = new Game(
            BOOK_ID,
            "game",
            20,
            2000,
            1,
            "Series",
            false
    );
    private static final Author AUTHOR = new Author(
            0,
            "firstName",
            "lastName",
            "nationality"
    );
    private static final List<Category> CATEGORIES = List.of(
            new Category(),
            new Category()
    );

    @InjectMocks
    private GameService gameServiceUnderTest;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private ICategoryService categoryService;
    @Mock
    private IAuthorService authorService;

    @Test
    void addGame() {
        when(gameRepository.save(BOOK)).thenReturn(BOOK);

        var result = gameServiceUnderTest.addGame(BOOK);
        verify(gameRepository, times(1)).save(BOOK);

        assertEquals(BOOK, result);
    }

    @Test
    void addGame_DatabaseError() {
        when(gameRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addGame(BOOK));
        verify(gameRepository, times(1)).save(any());
    }

    @Test
    void addAuthorToGame() {
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenReturn(AUTHOR);

        BOOK.setAuthor(AUTHOR);

        when(gameRepository.save(BOOK)).thenReturn(BOOK);

        var result = gameServiceUnderTest.addAuthorToGame(BOOK_ID, AUTHOR);
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        assertEquals(BOOK.getAuthor(), AUTHOR);
        verify(gameRepository, times(1)).save(BOOK);
        assertEquals(BOOK, result);
    }

    @Test
    void addAuthorToGame_NoSuchElementException() {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(NoFoundElementException.class);

        assertThrows(NoFoundElementException.class, () -> gameServiceUnderTest.addAuthorToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void addAuthorToGame_DatabaseError_at_findById(){
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addAuthorToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void addAuthorToGame_DeletedGameException() {
        BOOK.setIs_deleted(true);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        assertThrows(DeletedGameException.class, () ->  gameServiceUnderTest.addAuthorToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void addAuthorToGame_DatabaseError_at_save_author() {
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addAuthorToGame(BOOK_ID, AUTHOR));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        verify(gameRepository, never()).save(BOOK);
    }

    @Test
    void addAuthorToGame_DatabaseError_at_update_game() {
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenReturn(AUTHOR);

        BOOK.setAuthor(AUTHOR);

        when(gameRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addAuthorToGame(BOOK_ID, AUTHOR));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        assertEquals(BOOK.getAuthor(), AUTHOR);
        verify(gameRepository, times(1)).save(BOOK);
    }

    @Test
    void addCategoriesToGame() {
        BOOK.setGameCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        AtomicInteger categoryIdCounter = new AtomicInteger(1);
        when(categoryService.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(categoryIdCounter.getAndIncrement());
            return category;
        });

        when(gameRepository.save(BOOK)).thenReturn(BOOK);

        var result = gameServiceUnderTest.addCategoriesToGame(BOOK_ID, CATEGORIES);
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(CATEGORIES.size())).save(any(Category.class));
        assertEquals(new HashSet<>(BOOK.getGameCategories()), new HashSet<>(CATEGORIES));
        verify(gameRepository, times(1)).save(BOOK);
        assertEquals(BOOK, result);
        assertEquals(BOOK.getGameCategories().size(), CATEGORIES.size());
    }

    @Test
    void addCategoriesToGame_NoSuchElementException() {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(NoFoundElementException.class);

        assertThrows(NoFoundElementException.class, () -> gameServiceUnderTest.addCategoriesToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(gameRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToGame_DatabaseError_at_findById() {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addCategoriesToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(gameRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToGame_DeletedGameException() {
        BOOK.setIs_deleted(true);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        assertThrows(DeletedGameException.class, () ->  gameServiceUnderTest.addCategoriesToGame(BOOK_ID, any()));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(gameRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToGame_DatabaseError_at_save_category() {
        BOOK.setGameCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(categoryService.save(any(Category.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addCategoriesToGame(BOOK_ID, CATEGORIES));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(1)).save(any(Category.class));
        verify(gameRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToGame_DatabaseError_at_update_game() {
        BOOK.setGameCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        AtomicInteger categoryIdCounter = new AtomicInteger(1);
        when(categoryService.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(categoryIdCounter.getAndIncrement());
            return category;
        });

        when(gameRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.addCategoriesToGame(BOOK_ID, CATEGORIES));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(CATEGORIES.size())).save(any(Category.class));
        assertEquals(BOOK.getGameCategories(), CATEGORIES);
        verify(gameRepository, times(1)).save(BOOK);
    }

    @Test
    void updateGame() {
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        Game update_data = new Game(
                BOOK_ID,
                "update game",
                30,
                2000,
                0,
                null,
                false
        );
        BOOK.setName(update_data.getName());
        BOOK.setPrice(update_data.getPrice());
        BOOK.setSeries_name(update_data.getSeries_name());
        BOOK.setVolume(update_data.getVolume());
        BOOK.setYear(update_data.getYear());

        when(gameRepository.save(BOOK)).thenReturn(BOOK);

        var result = gameServiceUnderTest.updateGame(update_data, eq(BOOK_ID));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository).save(BOOK);

        assertEquals(BOOK, result);
    }

    @Test
    void updateGame_NoSuchElementException() {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(NoFoundElementException.class);

        assertThrows(NoFoundElementException.class, () -> gameServiceUnderTest.updateGame(new Game(), eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void updateGame_DatabaseError_at_findById() {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.updateGame(new Game(), eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void updateGame_DeletedGameException() {
        BOOK.setIs_deleted(true);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        assertThrows(DeletedGameException.class, () -> gameServiceUnderTest.updateGame(new Game(), eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void updateGame_DatabaseError_at_update_game() {
        BOOK.setIs_deleted(false);

        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(gameRepository.save(any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.updateGame(new Game(), eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, times(1)).save(any());
    }

    @Test
    void deleteGame() {
        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        BOOK.setIs_deleted(true);

        when(gameRepository.save(BOOK)).thenReturn(BOOK);

        gameServiceUnderTest.deleteGame(eq(BOOK_ID));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, times(1)).save(BOOK);
    }

    @Test
    void deleteGame_NoSuchElementException()  {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(NoFoundElementException.class);

        assertThrows(NoFoundElementException.class, () -> gameServiceUnderTest.deleteGame(eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void deleteGame_DatabaseError_at_findById()  {
        when(gameRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.deleteGame(eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void deleteGame_DatabaseError_at_update_game() {
        when(gameRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        BOOK.setIs_deleted(true);

        when(gameRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.deleteGame(eq(BOOK_ID)));
        verify(gameRepository, times(1)).findById(BOOK_ID);
        verify(gameRepository, times(1)).save(BOOK);
    }

    @Test
    void getGames() {
        List<Game> games = List.of(new Game(), new Game(), new Game());
        when(gameRepository.findAll()).thenReturn(games);

        var result = gameServiceUnderTest.getGames();
        assertEquals(games, result);
        assertEquals(games.size(), result.size());
    }

    @Test
    void getGames_DatabaseError() {
        when(gameRepository.findAll()).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.getGames());
    }

    @Test
    public void getAvailableGames() {
        List<Game> games = new ArrayList<>();
        games.add(new Game());
        games.add(new Game());

        // Mocking Pageable object for the first page with 10 results per page
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Game> mockPage = new PageImpl<>(games, pageable, games.size());

        when(gameRepository.getAvailableGames(pageable)).thenReturn(mockPage);

        List<Game> result = gameServiceUnderTest.getAvailableGames(0, 10);
        assertEquals(games, result);
    }

    @Test
    void getAvailableGames_DatabaseError() {
        List<Game> games = new ArrayList<>();
        games.add(new Game());
        games.add(new Game());

        // Mocking Pageable object for the first page with 10 results per page
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        // Mocking Page object with mockGames as content
        Page<Game> page = new PageImpl<>(games, pageable, games.size());

        when(gameRepository.getAvailableGames(pageable)).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.getAvailableGames(0, 10));
    }

    @Test
    void getGamesByAuthor() {
        BOOK.setAuthor(AUTHOR);
        List<Game> games = List.of(BOOK, BOOK);

        when(gameRepository.getGamesByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName())).thenReturn(games);

        var result = gameServiceUnderTest.getGamesByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName());
        verify(gameRepository, times(1)).getGamesByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName());
        assertEquals(games, result);
    }

    @Test
    void getGamesByAuthor_DatabaseError() {
        when(gameRepository.getGamesByAuthor(any(), any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.getGamesByAuthor(any(), any()));
        verify(gameRepository, times(1)).getGamesByAuthor(any(), any());
    }

    @Test
    void getGamesByCategory() {
        List<Game> games = List.of(BOOK, BOOK);

        when(gameRepository.getGamesByCategory(any())).thenReturn(games);

        var result = gameServiceUnderTest.getGamesByCategory(any());
        verify(gameRepository, times(1)).getGamesByCategory(any());
        assertEquals(games, result);
    }

    @Test
    void getGamesByCategory_DatabaseError() {
        when(gameRepository.getGamesByCategory(any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> gameServiceUnderTest.getGamesByCategory(any()));
        verify(gameRepository, times(1)).getGamesByCategory(any());
    }

    @Test
    void getGameById() {
        when(gameRepository.findById(BOOK_ID)).thenReturn(Optional.of(BOOK));

        var result = gameServiceUnderTest.getGameById(BOOK_ID);
        verify(gameRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK, result);
    }

    @Test
    void getGameById_NoSuchElementException() {
        when(gameRepository.findById(BOOK_ID)).thenThrow(NoFoundElementException.class);

        assertThrows(NoFoundElementException.class, () ->  gameServiceUnderTest.getGameById(BOOK_ID));
        verify(gameRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    void getGameById_DatabaseError() {
        when(gameRepository.findById(BOOK_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () ->  gameServiceUnderTest.getGameById(BOOK_ID));
        verify(gameRepository, times(1)).findById(BOOK_ID);
    }
}