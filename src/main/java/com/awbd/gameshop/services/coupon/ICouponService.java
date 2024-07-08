package com.awbd.gameshop.services.coupon;

import com.awbd.gameshop.models.Coupon;
import com.awbd.gameshop.models.User;

public interface ICouponService {
    Coupon findCoupon(int userId);

    void delete(Coupon coupon);

    Coupon insert(Double discount, User user);
}
