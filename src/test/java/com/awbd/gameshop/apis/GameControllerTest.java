package com.awbd.gameshop.apis;

import com.awbd.gameshop.dtos.RequestGame;
import com.awbd.gameshop.mappers.GameMapper;
import com.awbd.gameshop.models.*;
import com.awbd.gameshop.services.basket.IBasketService;
import com.awbd.gameshop.services.game.IGameService;
import com.awbd.gameshop.services.category.ICategoryService;
import com.awbd.gameshop.services.user.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("mysql")
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IGameService gameService;

    @MockBean
    private ICategoryService categoryService;

    @MockBean
    private IBasketService basketService;

    @MockBean
    private GameMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserService userService;
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void save() throws Exception{
        RequestGame game = new RequestGame("carte",20.3,2001,1,"serie");
        when(gameService.addGame(mapper.requestGame(game))).thenReturn(new Game("carte",20.3,2001,1,"serie",false));
        mockMvc.perform(post("/game")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","carte")
                        .param("price", String.valueOf(20.3))
                        .param("year",String.valueOf(2001))
                        .param("volume",String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mapper.requestGame(game)))
        ) .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));

    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void saveErr() throws Exception{
        RequestGame game = new RequestGame("carte",-1.0,2001,1,"serie");
        mockMvc.perform(post("/game")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("name","carte")
                        .param("price", String.valueOf(-1.0))
                        .param("year",String.valueOf(2001))
                        .param("volume",String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapper.requestGame(game)))
                ) .andExpect(model().attributeExists("game"))
                .andExpect(view().name("gameAddForm"));;

    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void addGame() throws Exception {
        this.mockMvc.perform(get("/game/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("game"))
                .andExpect(view().name("gameAddForm"));
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void saveGameUpdate() throws Exception{
        int id = 1;
        Game request = new Game("carte",20.3,2001,1,"serie",false);
        request.setId(id);
        Game game = new Game(id,"carte",20.3,2001,1,"serie",false);
        when(gameService.updateGame(request, id)).thenReturn(game);

        mockMvc.perform(post("/game/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("game",request)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
        verify(gameService,times(1)).updateGame(request,id);

    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void saveGameUpdateErr() throws Exception{
        int id = 1;
        Game request = new Game("carte",-1,2001,1,"serie",false);
        request.setId(id);
        Game game = new Game(id,"carte",-1,2001,1,"serie",false);
        mockMvc.perform(post("/game/update")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .flashAttr("game",request)
                )
                .andExpect(model().attributeExists("game"))
                .andExpect(view().name("gameForm"));
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void updateGame() throws Exception {
        int id = 1;
        Game game = new Game(id,"carte",20.3,2001,1,"serie",false);
        when(gameService.getGameById(id)).thenReturn(game);
        mockMvc.perform(get("/game/update/{id}","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("gameForm"))
                .andExpect(model().attribute("game",game));
        verify(gameService,times(1)).getGameById(id);
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void addAuthGame() throws Exception {
        int gameId = 1;
        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(gameId,"carte",20.3,2001,1,"serie",false,author);
        when(gameService.addAuthorToGame(gameId,author)).thenReturn(game);

        mockMvc.perform(post("/game/addAuthGame/{gameId}","1")
                        .param("firstName","Lara")
                        .param("lastName","Simon")
                        .param("nationality","Romanian")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void addAuthGameErr() throws Exception {
        int gameId = 1;
        Author author = new Author(1,"","Simon","Romanian");
        Game game = new Game(gameId,"carte",20.3,2001,1,"serie",false,author);
        when(gameService.getGameById(gameId)).thenReturn(game);

        mockMvc.perform(post("/game/addAuthGame/{gameId}","1")
                        .param("firstName","")
                        .param("lastName","Simon")
                        .param("nationality","Romanian")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author))
                )
                .andExpect(model().attributeExists("game"))
                .andExpect(view().name("gameAddAuthorToGame"));
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void addAuthorToGame() throws Exception{
        int gameId = 1;
        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(gameId,"carte",20.3,2001,1,"serie",false,author);

        when(gameService.getGameById(gameId)).thenReturn(game);

        this.mockMvc.perform(get("/game/addAuthorToGame/{gameId}","1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("game"))
                .andExpect(view().name("gameAddAuthorToGame"));

    }


    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void showAddCategoriesToGameForm() throws Exception{
        int gameId = 1;
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categoriesAll = new ArrayList<>();
        categoriesAll.add(category1);
        categoriesAll.add(category2);

        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(gameId,"carte",20.3,2001,1,"serie",false,author);

        when(gameService.getGameById(gameId)).thenReturn(game);
        when(categoryService.getCategories()).thenReturn(categoriesAll);

        this.mockMvc.perform(get("/game/addCategGame/{gameId}","1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("game",game))
                .andExpect(model().attribute("categoriesAll",categoriesAll))
                .andExpect(view().name("gameAddCategoriesToGame"));

    }


    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void addCategoriesToGame() throws Exception {
        int gameId = 1;
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(gameId,"carte",20.3,2001,1,"serie",false,author,categories);
        when(gameService.addCategoriesToGame(gameId,game.getGameCategories())).thenReturn(game);

        mockMvc.perform(post("/game/addCategoriesToGame/{gameId}","1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(game))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
    }

    @Test
    @WithMockUser(username = "miruna",password = "pass",roles = {"ADMIN"})
    public void deleteGame() throws Exception{
        int id=1;
        mockMvc.perform(delete("/game/delete/{id}","1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game"));
        verify(gameService).deleteGame(id);
    }

    @Test
    public void getAllGames() throws Exception {
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(1,"carte",20.3,2001,1,"serie",false,author,categories);

        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameService.getGames()).thenReturn(games);
        mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(view().name("gameList"))
                .andExpect(model().attribute("games",games));
    }

    @Test
    public void getAvailableGamesAnonymous() throws Exception {
        Integer pageNo = 0;
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categoriesAll = new ArrayList<>();
        categoriesAll.add(category1);
        categoriesAll.add(category2);

        when(categoryService.getCategories()).thenReturn(categoriesAll);
        int noAvailableGames = 1;
        when(gameService.numberAvailableGames()).thenReturn(noAvailableGames);
        Author author = new Author(1,"Lara","Simon","Romanian");
        Game game = new Game(1,"carte",20.3,2001,1,"serie",false,author,categoriesAll);

        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameService.getAvailableGames(pageNo,5)).thenReturn(games);
        int currentPage = 1;
        int userId = 0;

        when(gameService.getGames()).thenReturn(games);
        mockMvc.perform(get("/game/getAvailable/0")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalPages",1))
                .andExpect(model().attribute("nrAvailable",noAvailableGames))
                .andExpect(model().attribute("games",games))
                .andExpect(model().attribute("categoriesAll", categoriesAll))
                .andExpect(view().name("gameAvailableListNoLogin"));
    }

        @Test
        @WithMockUser(username = "miruna",password = "pass",roles = {"USER"})
        public void getAvailableGames() throws Exception {
            Integer pageNo = 0;
            Category category1 = new Category(1,"action");
            Category category2 = new Category(2,"romance");

            List<Category> categoriesAll = new ArrayList<>();
            categoriesAll.add(category1);
            categoriesAll.add(category2);

            when(categoryService.getCategories()).thenReturn(categoriesAll);
            int noAvailableGames = 1;
            when(gameService.numberAvailableGames()).thenReturn(noAvailableGames);
            Author author = new Author(1,"Lara","Simon","Romanian");
            Game game = new Game(1,"carte",20.3,2001,1,"serie",false,author,categoriesAll);

            List<Game> games = new ArrayList<>();
            games.add(game);

            when(gameService.getAvailableGames(pageNo,5)).thenReturn(games);

            int currentPage = 1;
            int userId = 3;
            when(userService.getCurrentUserId()).thenReturn(userId);

            User userul= new User("miruna","miruna@yahoo.com","pass","Miruna","Pos",true);
            userul.setId(userId);

            Basket basket = new Basket(3,false,51.0,userul);
            when(basketService.getBasket(userId)).thenReturn(basket);
            when(gameService.getGames()).thenReturn(games);
            mockMvc.perform(get("/game/getAvailable/0")
                    )
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("totalPages",1))
                    .andExpect(model().attribute("nrAvailable",noAvailableGames))
                    .andExpect(view().name("gameAvailableList"))
                   .andExpect(model().attribute("categoriesAll", categoriesAll))
                    .andExpect(model().attribute("games",games))
                    .andExpect(model().attribute("basket",basket));
        }

    @Test
    public void getGamesByCategory() throws Exception{
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categoriesAll = new ArrayList<>();
        categoriesAll.add(category1);
        categoriesAll.add(category2);
        Author author1 = new Author(1,"Lara","Simoni","Romanian");
        when(categoryService.getCategories()).thenReturn(categoriesAll);

        Game game = new Game(1,"carte",20.0,2001,1,"serie",false,author1,categoriesAll);
        List<Game> games = new ArrayList<>();
        games.add(game);
        when(gameService.getGamesByCategory("action")).thenReturn(games);

        mockMvc.perform(get("/game/getGamesByCategory")
                        .param("selectedCategory","action"))
                .andExpect(status().isOk())
                .andExpect(view().name("gamesByCateg"))
                .andExpect(model().attribute("categoriesAll",categoriesAll))
                .andExpect(model().attribute("games",games))
                .andExpect(model().attribute("selectedCategory","action"));

    }

    @Test
    public void getGamesByAuthor() throws Exception{
        String firstName = "Lara";
        String lastName = "Simoni";
        Category category1 = new Category(1,"action");
        Category category2 = new Category(2,"romance");

        List<Category> categoriesAll = new ArrayList<>();
        categoriesAll.add(category1);
        categoriesAll.add(category2);
        Author author1 = new Author(1,firstName,lastName,"Romanian");

        Game game = new Game(1,"carte",20.0,2001,1,"serie",false,author1,categoriesAll);
        List<Game> games = new ArrayList<>();
        games.add(game);

        when(gameService.getGamesByAuthor(firstName,lastName)).thenReturn(games);
        when(categoryService.getCategories()).thenReturn(categoriesAll);


        mockMvc.perform(get("/game/getGamesByAuthor/{firstname}/{lastName}","Lara","Simoni")
                        )
                .andExpect(status().isOk())
                .andExpect(view().name("gamesByAuthor"))
                .andExpect(model().attribute("categoriesAll",categoriesAll))
                .andExpect(model().attribute("games",games))
                .andExpect(model().attribute("firstname","Lara"))
                .andExpect(model().attribute("lastname","Simoni"));

    }

}
