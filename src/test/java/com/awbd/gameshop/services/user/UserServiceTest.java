package com.awbd.gameshop.services.user;

import com.awbd.gameshop.dtos.UpdateUser;
import com.awbd.gameshop.dtos.UserDetails;
import com.awbd.gameshop.exceptions.exceptions.EmailAlreadyUsedException;
import com.awbd.gameshop.exceptions.exceptions.NoFoundElementException;
import com.awbd.gameshop.models.User;
import com.awbd.gameshop.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    @Test
    public void create_NewUser() {
        User newUser = new User("TestUser", "test@example.com", "password");

        when(userRepository.getUserByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        User createdUser = userServiceUnderTest.create(newUser);
        assertNotNull(createdUser);
        assertEquals(newUser, createdUser);
    }

    @Test
    public void create_UserAlreadyExists() {
        User existingUser = new User("existing@example.com", "password", "ExistingUser");
        User newUser = new User("existing@example.com", "password", "TestUser");

        when(userRepository.getUserByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceUnderTest.create(newUser));
    }

    @Test
    public void update_UserFound() {
        Integer userId = 123;
        UpdateUser updateUser = new UpdateUser("UpdatedUser", "Updated", "User");
        User existingUser = new User("test@example.com", "password", "TestUser");
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userServiceUnderTest.update(userId, updateUser);
        assertNotNull(updatedUser);
        assertEquals(updateUser.getUsername(), updatedUser.getUsername());
        assertEquals(updateUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(updateUser.getLastName(), updatedUser.getLastName());
    }

    @Test
    public void update_UserNotFound() {
        Integer userId = 123;
        UpdateUser updateUser = new UpdateUser("UpdatedUser", "Updated", "User");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoFoundElementException.class, () -> userServiceUnderTest.update(userId, updateUser));
    }

    @Test
    public void getUsers() {
        List<UserDetails> expectedUsers = new ArrayList<>();
        expectedUsers.add(new UserDetails(1, "First1", "Last1", "User1", "user1@example.com"));
        expectedUsers.add(new UserDetails(2, "First2", "Last2", "User2", "user2@example.com"));

        when(userRepository.getUsers()).thenReturn(expectedUsers);

        List<UserDetails> actualUsers = userServiceUnderTest.getUsers();
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0), actualUsers.get(0));
        assertEquals(expectedUsers.get(1), actualUsers.get(1));
    }

    @Test
    public void delete_UserFound() {
        Integer userId = 123;
        User existingUser = new User("test@example.com", "password", "TestUser");
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User deletedUser = userServiceUnderTest.delete(userId);
        assertNotNull(deletedUser);
        assertFalse(deletedUser.isEnabled());
    }

    @Test
    public void delete_UserNotFound() {
        Integer userId = 123;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoFoundElementException.class, () -> userServiceUnderTest.delete(userId));
    }

    @Test
    public void getUser_UserFound() {
        Integer userId = 123;
        User expectedUser = new User("test@example.com", "password", "TestUser");
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userServiceUnderTest.getUser(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getUser_UserNotFound() {
        Integer userId = 123;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoFoundElementException.class, () -> userServiceUnderTest.getUser(userId));
    }

    @Test
    public void getId(){
        String username = "ana";
        int userId = 1;
        when(userRepository.findByUserName(username)).thenReturn(userId);
        int userIdActual = userServiceUnderTest.getId(username);
        assertNotNull(userIdActual);
        assertEquals(userId,userIdActual);
    }

    @Test
    public void getCurrentUserIdAnonymous(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("anonymousUser");

        Integer userId = userServiceUnderTest.getCurrentUserId();
        assertEquals(0,userId);
    }

    @Test
    public void getCurrentUserIdUnauthenticated(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(null);

        Integer userId = userServiceUnderTest.getCurrentUserId();
        assertEquals(0,userId);
    }

    @Test
    public void getCurrentUserIdAuthenticated(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("ROLE_USER");
        when(userServiceUnderTest.getId("ROLE_USER")).thenReturn(1);
        Integer userId = userServiceUnderTest.getCurrentUserId();
        assertEquals(1,userId);
    }
}