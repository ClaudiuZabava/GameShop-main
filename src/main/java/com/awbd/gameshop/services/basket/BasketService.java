package com.awbd.gameshop.services.basket;

import com.awbd.gameshop.dtos.GameFromBasketDetails;
import com.awbd.gameshop.exceptions.exceptions.NoFoundElementException;
import com.awbd.gameshop.models.Basket;
import com.awbd.gameshop.models.Coupon;
import com.awbd.gameshop.models.User;
import com.awbd.gameshop.repositories.BasketRepository;
import com.awbd.gameshop.services.gamebasket.IGameBasketService;
import com.awbd.gameshop.services.coupon.ICouponService;
import com.awbd.gameshop.services.user.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;
    private final IGameBasketService gameBasketService;
    private final IUserService userService;
    private  final ICouponService couponService;

    public BasketService(
            BasketRepository basketRepository,
            IGameBasketService gameBasketService,
            IUserService userService,
            ICouponService couponService) {
        this.basketRepository = basketRepository;
        this.gameBasketService = gameBasketService;
        this.userService = userService;
        this.couponService = couponService;
    }

    @Transactional
    @Override
    public Basket createBasket(int userId) {
        User user = userService.getUser(userId);

        Basket basket = basketRepository.findByUserId(userId).orElse(null);

        if (basket == null) {
            return basketRepository.save(new Basket(
                    0,
                    false,
                    0,
                    user
            ));
        }

        return basket;
    }

    @Transactional
    @Override
    public Basket sentOrder(int userId) {
        Basket basket = basketRepository.findByUserId(userId).orElseThrow(
                () -> new NoFoundElementException("User does not have a current basket"));

        if (basket.getCost() == 0)
            throw new NoFoundElementException("User does not have games in basket");

        // coupon logic
        Coupon coupon = couponService.findCoupon(userId);

        if (coupon != null) {
            basket.setCost((1 - coupon.getDiscount()/100) * basket.getCost());
            coupon.setUser(null);
            couponService.delete(coupon);
        }

        if (basket.getCost() > 100) {
            User user = userService.getUser(userId);
            couponService.insert(10.0, user);
        }

        basket.setSent(true);
        return basketRepository.save(basket);
    }

    @Transactional
    @Override
    public Basket getBasket(int userId) {
        Basket basket = basketRepository.findByUserId(userId).orElse(null);

        if (basket == null)
            basket = basketRepository.save(new Basket(
                    0,
                    false,
                    0,
                    userService.getUser(userId)
            ));

        return basket;
    }

    @Transactional
    @Override
    public List<GameFromBasketDetails> findGamesFromCurrentBasket(int basketId) {
        return basketRepository.findGamesFromCurrentBasket(basketId);
    }

    @Transactional
    @Override
    public Basket addGameToBasket(int gameId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoFoundElementException("Does not exist a basket with this id")
        );

        Double gamePriceInBasket = gameBasketService.addGameToBasket(gameId, basket);

        basket.setCost(basket.getCost() + gamePriceInBasket);
        return basketRepository.save(basket);
    }

    @Transactional
    @Override
    public Basket removeGameFromBasket(int gameId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoFoundElementException("Does not exist a basket with this id"));

        Double gamePriceInBasket = gameBasketService.removeGameToBasket(gameId, basketId);

        basket.setCost(basket.getCost() - gamePriceInBasket);
        return basketRepository.save(basket);
    }

    @Transactional
    @Override
    public Basket decrementGameFromBasket(int gameId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoFoundElementException("Does not exist a basket with this id"));

        Double gamePriceInBasket = gameBasketService.decrementGameFromBasket(gameId, basketId);

        basket.setCost(basket.getCost() - gamePriceInBasket);
        return basketRepository.save(basket);
    }
}
