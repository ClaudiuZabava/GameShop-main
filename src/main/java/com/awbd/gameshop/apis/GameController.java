package com.awbd.gameshop.apis;

import com.awbd.gameshop.dtos.RequestGame;
import com.awbd.gameshop.mappers.GameMapper;
import com.awbd.gameshop.models.Author;
import com.awbd.gameshop.models.Basket;
import com.awbd.gameshop.models.Game;
import com.awbd.gameshop.models.Category;
import com.awbd.gameshop.services.basket.IBasketService;
import com.awbd.gameshop.services.game.IGameService;
import com.awbd.gameshop.services.category.ICategoryService;
import com.awbd.gameshop.services.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    final IGameService gameService;
    final GameMapper mapper;
    final ICategoryService categoryService;
    final IBasketService basketService;
    final IUserService userService;
    public GameController(IGameService gameService, GameMapper mapper, ICategoryService categoryService, IBasketService basketService, IUserService userService) {
        this.gameService = gameService;
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @PostMapping("")
    public ModelAndView save(
            @Valid @ModelAttribute("game") RequestGame newGame,
            BindingResult bindingResult,
            Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("game",newGame);
            return new ModelAndView("gameAddForm");}

        gameService.addGame(mapper.requestGame(newGame));

        return new ModelAndView("redirect:/game");
    }

    @RequestMapping("/add")
    public ModelAndView addGame(Model model){
        model.addAttribute("game",new Game());
        return new ModelAndView("gameAddForm");
    }

    @PostMapping("/update")
    public ModelAndView saveGameUpdate(
            @Valid @ModelAttribute("game") Game game,
            BindingResult bindingResult,
            Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("game",game);
            return new ModelAndView("gameForm");}
        gameService.updateGame(game,game.getId());
        return new ModelAndView("redirect:/game");
    }
    @RequestMapping("/update/{id}")
    public ModelAndView updateGame(
            @PathVariable int id,
            Model model){

        model.addAttribute("game",gameService.getGameById(id));
        return new ModelAndView("gameForm");
    }

    @PostMapping("/addAuthGame/{gameId}")
    public ModelAndView addAuthGame(
            @PathVariable int gameId,
            @Valid @ModelAttribute("author") Author author,
            BindingResult bindingResult,
            Model model){
        if(bindingResult.hasErrors()){
            Game game = gameService.getGameById(gameId);
            if (game != null && game.getAuthor() != null) {
                model.addAttribute("author", game.getAuthor());
            }
            model.addAttribute("game",gameService.getGameById(gameId));
            return new ModelAndView("gameAddAuthorToGame");
        }

        gameService.addAuthorToGame(gameId,author);
        return new ModelAndView("redirect:/game");
    }
    @RequestMapping("/addAuthorToGame/{gameId}")
    public ModelAndView addAuthorToGame(
            @PathVariable int gameId,
            @Valid Model model,
            Author author) {
        Game game = gameService.getGameById(gameId);
        if (game != null && game.getAuthor() != null) {
            model.addAttribute("author", game.getAuthor());
        }
        model.addAttribute("game",gameService.getGameById(gameId));
        return new ModelAndView("gameAddAuthorToGame");

    }

    @RequestMapping("/addCategGame/{gameId}")
    public ModelAndView showAddCategoriesToGameForm(@PathVariable int gameId, Model model) {
        model.addAttribute("game",gameService.getGameById(gameId));
        List<Category> categoriesAll = categoryService.getCategories();

        model.addAttribute("categoriesAll", categoriesAll);
        return new ModelAndView("gameAddCategoriesToGame");
    }

    @PostMapping("/addCategoriesToGame/{gameId}")
    public ModelAndView addCategoriesToGame(
            @PathVariable int gameId,
            @ModelAttribute Game game
            ){

        gameService.addCategoriesToGame(gameId, game.getGameCategories());
        return new ModelAndView("redirect:/game");
    }

    @RequestMapping("/delete/{id}")
    public ModelAndView deleteGame(
            @PathVariable int id
    ){
        gameService.deleteGame(id);
        return new ModelAndView("redirect:/game");
    }

    @RequestMapping("")
    public ModelAndView getAllGames(Model model){
        List<Game> games = gameService.getGames();
        model.addAttribute("games",games);
        return new ModelAndView ("gameList");
    }

    @RequestMapping("/getAvailable/{pageNo}")
    public ModelAndView getAvailableGames(Model model,
                                          @PathVariable Integer pageNo
                                          ){
        List<Category> categoriesAll = categoryService.getCategories();
        model.addAttribute("categoriesAll", categoriesAll);
        List<Game> games = gameService.getAvailableGames(pageNo, 5);
        model.addAttribute("games",games);
        model.addAttribute(pageNo);
        int noAvailableGames = gameService.numberAvailableGames();
        model.addAttribute("nrAvailable",noAvailableGames);
        if(noAvailableGames<=5)
            model.addAttribute("totalPages",1);
        else
            model.addAttribute("totalPages",(int) Math.ceil((double) noAvailableGames / 5));
        int currentPage = pageNo > 0 ? pageNo : 0;
        model.addAttribute(currentPage);

        int userId = userService.getCurrentUserId();

       if(userId==0)
           return new ModelAndView("gameAvailableListNoLogin");
        else
       {
            Basket basket = basketService.getBasket(userId);
            model.addAttribute("basket",basket);
            return new ModelAndView ("gameAvailableList");
       }
    }

    @GetMapping("/getGamesByCategory")
    public ModelAndView getGamesByCategory(
            @RequestParam(name = "selectedCategory") String category,
            Model model) {
        List<Category> categoriesAll = categoryService.getCategories();
        model.addAttribute("categoriesAll", categoriesAll);
        List<Game> games = gameService.getGamesByCategory(category);
        model.addAttribute("games",games);
        model.addAttribute("selectedCategory", category);
        return new ModelAndView("gamesByCateg");
    }

    @GetMapping("/getGamesByAuthor/{firstname}/{lastName}")
    public ModelAndView getGamesByAuthor(
            @PathVariable String firstname,
            @PathVariable String lastName,
            Model model) {
        List<Game> games = gameService.getGamesByAuthor(firstname, lastName);
        model.addAttribute("games",games);
        List<Category> categoriesAll = categoryService.getCategories();
        model.addAttribute("categoriesAll", categoriesAll);
        model.addAttribute("firstname",firstname);
        model.addAttribute("lastname",lastName);
        return new ModelAndView("gamesByAuthor");
    }

}
