package com.awbd.gameshop.services.coupon;

import com.awbd.gameshop.models.Coupon;
import com.awbd.gameshop.models.User;
import com.awbd.gameshop.repositories.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService implements ICouponService {
    final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon findCoupon(int userId) {
        return couponRepository.findByUser(userId).orElse(null);
    }

    @Override
    public void delete(Coupon coupon) {
        couponRepository.delete(coupon);
    }

    @Override
    public Coupon insert(Double discount, User user) {
        return couponRepository.save(new Coupon(discount, user));
    }
}
