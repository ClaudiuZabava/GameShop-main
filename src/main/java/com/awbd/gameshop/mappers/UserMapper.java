package com.awbd.gameshop.mappers;

import com.awbd.gameshop.dtos.RequestUser;
import com.awbd.gameshop.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User requestUser(RequestUser user) {
        return new User(
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            true
        );
    }
}
