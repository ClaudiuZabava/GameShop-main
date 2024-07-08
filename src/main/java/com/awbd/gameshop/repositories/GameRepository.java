package com.awbd.gameshop.repositories;

import com.awbd.gameshop.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("SELECT b FROM Game b WHERE b.is_deleted = false")
    Page<Game> getAvailableGames(Pageable pageable);

    @Query("SELECT DISTINCT b FROM Game b JOIN b.gameCategories c WHERE c.name = :category AND b.is_deleted = false")
    List<Game> getGamesByCategory(String category);

    @Query("SELECT DISTINCT b FROM Game b JOIN b.author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND b.is_deleted = false")
    List<Game> getGamesByAuthor(String firstName, String lastName);

    Game save(Game newGame);

    List<Game> findAll();

    Optional<Game> findById(Integer gameId);

    @Query("SELECT b FROM Game b WHERE b.is_deleted = false")
    List<Game> getAllAvailableGames();
}
