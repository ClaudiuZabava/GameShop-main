package com.awbd.gameshop.services.user;

import com.awbd.gameshop.dtos.UpdateUser;
import com.awbd.gameshop.dtos.UserDetails;
import com.awbd.gameshop.dtos.UserResponse;
import com.awbd.gameshop.models.User;

import java.util.List;

public interface IUserService {
    User create(User user);

    User update(Integer id, UpdateUser user);

    UserResponse login(String email, String password);

    List<UserDetails> getUsers();

    User delete(Integer id);

    User getUser(int userId);

    int getId(String username);

    Integer getCurrentUserId();
}
