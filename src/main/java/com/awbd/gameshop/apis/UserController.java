package com.awbd.gameshop.apis;


import com.awbd.gameshop.dtos.RequestUser;
import com.awbd.gameshop.dtos.UpdateUser;
import com.awbd.gameshop.dtos.UserDetails;
import com.awbd.gameshop.mappers.UserMapper;
import com.awbd.gameshop.models.Authority;
import com.awbd.gameshop.models.User;
import com.awbd.gameshop.repositories.AuthorityRepository;
import com.awbd.gameshop.services.authority.IAuthorityService;
import com.awbd.gameshop.services.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    final IUserService userService;
    final UserMapper mapper;
    private PasswordEncoder passwordEncoder;

    private IAuthorityService authorityService;

    public UserController(IUserService userService, UserMapper mapper, PasswordEncoder passwordEncoder,
                         IAuthorityService authorityService) {
        this.userService = userService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
    }

    @PostMapping("/signUp")
    public ModelAndView add(
            @Valid @ModelAttribute RequestUser newUser
            ){
        User user = mapper.requestUser(newUser);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Authority authority = authorityService.getAuthorityByName("ROLE_USER");
        user.setAuthority(authority);

        userService.create(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/form")
    public ModelAndView create(Model model){
        model.addAttribute("user",new User());
        return new ModelAndView("register");
    }

}