package com.awbd.gameshop.apis;

import com.awbd.gameshop.dtos.BasketDetails;
import com.awbd.gameshop.dtos.GameFromBasketDetails;
import com.awbd.gameshop.mappers.BasketMapper;
import com.awbd.gameshop.models.Basket;
import com.awbd.gameshop.services.basket.IBasketService;
import com.awbd.gameshop.services.user.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {
    final IBasketService basketService;
    final BasketMapper mapper;
    final IUserService userService;

    public BasketController(IBasketService basketService, BasketMapper mapper, IUserService userService) {
        this.basketService = basketService;
        this.mapper = mapper;
        this.userService = userService;
    }

    @RequestMapping("/sentOrder")
    public ModelAndView sentOrder(Model model)
    {
        int userId = userService.getCurrentUserId();
        Basket basket = basketService.sentOrder(userId);
        model.addAttribute(basket);
        List<GameFromBasketDetails> games = basketService.findGamesFromCurrentBasket(basket.getId());
        model.addAttribute(games);
        return new ModelAndView("redirect:/basket/myBasket");
    }

    @GetMapping("/myBasket")
    public ModelAndView getBasket(Model model)
    {
        int userId = userService.getCurrentUserId();
        Basket basket = basketService.getBasket(userId);
        model.addAttribute("basket",basket);
        List<GameFromBasketDetails> games = basketService.findGamesFromCurrentBasket(basket.getId());
        model.addAttribute("games",games);
        return new ModelAndView("basketView");
    }

    @RequestMapping("/addGameToBasket/{gameId}/{basketId}")
    public  ModelAndView addGameToBasket(
            @PathVariable int gameId,
            @PathVariable int basketId,
            Model model
    ) {
        Basket basket = basketService.addGameToBasket(gameId, basketId);
        List<GameFromBasketDetails> games = basketService.findGamesFromCurrentBasket(basketId);
        model.addAttribute(basket);
        model.addAttribute(games);
        return new ModelAndView("redirect:/basket/myBasket");
    }

    @RequestMapping("/removeGameFromBasket/{gameId}/{basketId}")
    public  ModelAndView removeGameFromBasket(
            @PathVariable int gameId,
            @PathVariable int basketId,
            Model model) {
        Basket basket = basketService.removeGameFromBasket(gameId, basketId);
        model.addAttribute("basket",basket);
        List<GameFromBasketDetails> games = basketService.findGamesFromCurrentBasket(basketId);
        model.addAttribute("games",games);
        return new ModelAndView("redirect:/basket/myBasket");
    }

    @RequestMapping("/decrementGameFromBasket/{gameId}/{basketId}")
    public ModelAndView decrementGameFromBasket(
            @PathVariable int gameId,
            @PathVariable int basketId,
            Model model
    ) {
        Basket basket = basketService.decrementGameFromBasket(gameId, basketId);
        model.addAttribute(basket);
        List<GameFromBasketDetails> games = basketService.findGamesFromCurrentBasket(basketId);
        model.addAttribute(games);
        return new ModelAndView("redirect:/basket/myBasket");
    }
}
