package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exeption.UserFriendServiceException;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.EventService;
import ru.yandex.practicum.filmorate.service.user.UserFriendService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    EventService eventService;

    @MockBean
    @Qualifier("userFriendServiceDbImpl")
    UserFriendService userFriendService;

    @Test
    @DisplayName("GET /users возвращает коллекцию из двух пользователей ")
    void getAllUserTest() throws Exception {
        var requestBuilder = get("/users");
        Mockito.when(userService.getUsers()).thenReturn(List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        ));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1," +
                        "\"email\":\"testEmail1@test.com\"," +
                        "\"login\":\"testLogin1\"," +
                        "\"name\":\"testName1\"," +
                        "\"birthday\":\"1989-03-05\"}," +
                        "{\"id\":2," +
                        "\"email\":\"testEmail2@test.com\"," +
                        "\"login\":\"testLogin2\"," +
                        "\"name\":\"testName2\"," +
                        "\"birthday\":\"1989-10-05\"}]"));
    }

    @Test
    @DisplayName("POST /users создаёт нового пользователя и возвращает статус 201")
    void addNewUserTestValid() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}");
        Mockito.when(userService.addUser(Mockito.any())).thenReturn(User.builder()
                .id(1)
                .birthday(LocalDate.parse("2020-03-04"))
                .name("validUserName")
                .email("validuser@test.com")
                .login("validUserLogin")
                .build());

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}"),
                jsonPath("$.id").exists()
        );
    }

    @Test
    @DisplayName("POST /users не создаёт нового пользователя c неправильным email и возвращает статус 404 и сообщение ошибки в тело ответа")
    void addNewUserTestNotValidEmail() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content("{\"email\":\"@validUser\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"must be a well-formed email address\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST /users не создаёт нового пользователя c пустым логином и возвращает статус 404 и сообщение ошибки в тело ответа")
    void addNewUserTestValidLogin() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"\"" +
                        ",\"name\":\"validUserName\"}");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Логин пользователя не должен быть пустым\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST /users создаёт нового пользователя c пустым именем")
    void addNewUserTestValidEmptyName() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"\"}");

        Mockito.when(userService.addUser(Mockito.any())).thenReturn(
                User.builder()
                        .id(1)
                        .name("validUserLogin")
                        .login("validUserLogin")
                        .birthday(LocalDate.parse("2020-03-04"))
                        .email("validuser@test.com")
                        .build()
        );

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserLogin\"}"),
                jsonPath("$.id").exists()
        );
    }

    @Test
    @DisplayName("POST /users не создаёт нового пользователя c датой рождения в будующем")
    void addNewUserTestNotValidBirthday() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content(String.format("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"%s\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}", LocalDate.now().plusDays(1)));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Дата рождения не должна быть в будущем\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users обновляет пользователя")
    void updateUserTestValid() throws Exception {
        var requestBuilder = put("/users")
                .contentType(APPLICATION_JSON).content("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserNameUpdate\"}");

        Mockito.when(userService.updateUser(Mockito.any())).thenReturn(
                User.builder()
                        .id(1)
                        .login("validUserLoginUpdate")
                        .email("validuserUpdate@test.com")
                        .name("validUserNameUpdate")
                        .birthday(LocalDate.parse("2020-03-04"))
                        .build());

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserNameUpdate\"" +
                        ",\"birthday\":\"2020-03-04\"}")
        );
    }

    @Test
    @DisplayName("PUT /users не обновляет пользователя с неправильным email")
    void updateUserTestNotValidEmail() throws Exception {
        var requestBuilder = put("/users")
                .contentType(APPLICATION_JSON).content("{\"id\":1" +
                        ",\"email\":\"notvaliduserUpdatetest.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserNameUpdate\"}");


        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"must be a well-formed email address\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users не обновляет пользователя с неправильным email")
    void updateUserTestNotValidLogin() throws Exception {
        var requestBuilder = put("/users")
                .contentType(APPLICATION_JSON).content("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"\"" +
                        ",\"name\":\"validUserNameUpdate\"}");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Логин пользователя не должен быть пустым\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users обновляет пользователя с пустым именем")
    void updateUserTestValidEmptyName() throws Exception {
        var requestBuilder = put("/users")
                .contentType(APPLICATION_JSON).content("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"\"}");

        Mockito.when(userService.updateUser(Mockito.any()))
                .thenReturn(User.builder()
                        .id(1)
                        .email("validuserUpdate@test.com")
                        .name("validUserLoginUpdate")
                        .login("validUserLoginUpdate")
                        .birthday(LocalDate.parse("2020-03-04"))
                        .build());

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserLoginUpdate\"" +
                        ",\"birthday\":\"2020-03-04\"}")
        );
    }

    @Test
    @DisplayName("PUT /users не обновляет пользователя с датой рождения в будующем")
    void updateUserTestNotValidBirthday() throws Exception {
        var requestBuilder = put("/users")
                .contentType(APPLICATION_JSON).content(String.format("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"birthday\":\"%s\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserNameUpdate\"}", LocalDate.now().plusDays(1)));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Дата рождения не должна быть в будущем\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 1 добавляет в друзья пользователя 2")
    void addUserToFriendsTest_User1AddToFriendsUser2() throws Exception {
        var requestBuilder = put("/users/1/friends/2");

        Mockito.when(userFriendService.addingUserAsFriend(1, 2)).thenReturn(" ");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 2 не существует")
    void addUserToFriendsTest_User1NotExists() throws Exception {
        var requestBuilder = put("/users/1/friends/2");

        Mockito.when(userFriendService.addingUserAsFriend(1, 2)).thenThrow(
                new UserFriendServiceException("Попытка добавить в друзья несуществующего пользователя с id: 2"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().json("{\"Ошибка работы с друзьями\"" +
                        ":\"Попытка добавить в друзья несуществующего пользователя с id: 2\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 1 не существует")
    void addUserToFriendsTest_User2NotExists() throws Exception {
        var requestBuilder = put("/users/1/friends/2");

        Mockito.when(userFriendService.addingUserAsFriend(1, 2)).thenThrow(
                new UserFriendServiceException("Попытка добавить друга для несуществующего пользователя с id: 1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().json("{\"Ошибка работы с друзьями\"" +
                        ":\"Попытка добавить друга для несуществующего пользователя с id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1 возвращает пользователя с id 1")
    void getUserByIdTest_GetUser1() throws Exception {
        var requestBuilder = get("/users/1");

        Mockito.when(userService.getUserById(1)).thenReturn(
                User.builder()
                        .id(1)
                        .email("testEmail@test.com1")
                        .name("testName1")
                        .login("testLogin1")
                        .birthday(LocalDate.parse("1990-01-02"))
                        .build()
        );

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1" +
                        ",\"email\":\"testEmail@test.com1\"" +
                        ",\"login\":\"testLogin1\"" +
                        ",\"name\":\"testName1\"" +
                        ",\"birthday\":\"1990-01-02\"}")
        );
    }

    @Test
    @DisplayName("GET /users/1 не возвращает не существующего пользователя с id 1")
    void getUserByIdTest_GetUser1NotExists() throws Exception {
        var requestBuilder = get("/users/1");

        Mockito.when(userService.getUserById(1)).thenThrow(
                new UserServiceException("Попытка получить пользователя с несуществующим id: 1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с пользователями\":\"Попытка получить пользователя с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1 удаляет пользователя с id 1")
    void deleteFromUserFriendsTest_DeleteUser1() throws Exception {
        var requestBuilder = delete("/users/1");

        Mockito.when(userService.deleteUserById(1)).thenReturn("Удалён пользователь с id: 1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Удалён пользователь с id: 1")

        );
    }

    @Test
    @DisplayName("DELETE /users/1 не удаляет несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_DeleteUser1NotExists() throws Exception {
        var requestBuilder = delete("/users/1");

        Mockito.when(userService.deleteUserById(1)).thenThrow(
                new UserServiceException("Попытка удалить пользователя с несуществующим id: 1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с пользователями\":\"Попытка удалить пользователя с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends возвращает список друзей для пользователя с id 1")
    void getUserFriendsTest_ReturnListFriends() throws Exception {
        var requestBuilder = get("/users/1/friends");

        Mockito.when(userFriendService.getUserFriends(1)).thenReturn(List.of(
                User.builder()
                        .id(2)
                        .login("testLogin2")
                        .name("testName2")
                        .email("testEmail@test.com2")
                        .birthday(LocalDate.parse("1990-01-03"))
                        .build(),
                User.builder()
                        .id(3)
                        .login("testLogin3")
                        .name("testName3")
                        .email("testEmail@test.com3")
                        .birthday(LocalDate.parse("1990-01-04"))
                        .build()
        ));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":2" +
                        ",\"email\":\"testEmail@test.com2\"" +
                        ",\"login\":\"testLogin2\"" +
                        ",\"name\":\"testName2\"" +
                        ",\"birthday\":\"1990-01-03\"}," +
                        "{\"id\":3" +
                        ",\"email\":\"testEmail@test.com3\"" +
                        ",\"login\":\"testLogin3\"" +
                        ",\"name\":\"testName3\"" +
                        ",\"birthday\":\"1990-01-04\"}]")
        );
    }

    @Test
    @DisplayName("GET /users/1/friends не возвращает список друзей для несуществующего пользователя с id 1")
    void getUserFriendsTest_User1NotExists() throws Exception {
        var requestBuilder = get("/users/1/friends");

        Mockito.when(userFriendService.getUserFriends(1))
                .thenThrow(new UserFriendServiceException(
                        "Попытка получить список друзей для несуществующего пользователя с id: 1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\"" +
                        ":\"Попытка получить список друзей для несуществующего пользователя с id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 возвращает список общих друзей для пользователя с id 1 и пользователя с id 2")
    void getUsersCommonFriendsTest_ReturnsListCommonFriends() throws Exception {
        var requestBuilder = get("/users/1/friends/common/2");

        Mockito.when(userFriendService.getListOfCommonFriends(1, 2)).thenReturn(
                List.of(
                        User.builder()
                                .id(4)
                                .login("testLogin4")
                                .name("testName4")
                                .email("testEmail@test.com4")
                                .birthday(LocalDate.parse("1990-01-05"))
                                .build(),
                        User.builder()
                                .id(5)
                                .login("testLogin5")
                                .name("testName5")
                                .email("testEmail@test.com5")
                                .birthday(LocalDate.parse("1990-01-06"))
                                .build()
                )
        );

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":4" +
                        ",\"email\":\"testEmail@test.com4\"" +
                        ",\"login\":\"testLogin4\"" +
                        ",\"name\":\"testName4\"" +
                        ",\"birthday\":\"1990-01-05\"}" +
                        ",{\"id\":5" +
                        ",\"email\":\"testEmail@test.com5\"" +
                        ",\"login\":\"testLogin5\"" +
                        ",\"name\":\"testName5\"" +
                        ",\"birthday\":\"1990-01-06\"}]")
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 не возвращает список общих друзей для несуществующего пользователя с id 1")
    void getUsersCommonFriendsTest_User1NotExists() throws Exception {
        var requestBuilder = get("/users/1/friends/common/2");

        Mockito.when(userFriendService.getListOfCommonFriends(1, 2)).thenThrow(
                new UserFriendServiceException("Попытка получить список общих друзей для несуществующего пользователя с id: 1")
        );

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\"" +
                        ":\"Попытка получить список общих друзей для несуществующего пользователя с id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 не возвращает список общих друзей для пользователя с id 1 " +
            " и не существующего пользователя с id 2")
    void getUsersCommonFriendsTest_User2NotExists() throws Exception {
        var requestBuilder = get("/users/1/friends/common/2");

        Mockito.when(userFriendService.getListOfCommonFriends(1, 2)).thenThrow(
                new UserFriendServiceException("Попытка получить список общих друзей несуществующего пользователя с id: 2")
        );

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\":" +
                        "\"Попытка получить список общих друзей несуществующего пользователя с id: 2\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1 удаляет пользователя с id 1")
    void deleteUserTest() throws Exception {
        var requestBuilder = delete("/users/1");

        Mockito.when(userService.deleteUserById(1)).thenReturn("Удалён пользователь с id: 1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Удалён пользователь с id: 1")
        );
    }

    @Test
    @DisplayName("DELETE /users/1 не удаляет несуществующего пользователя с id 1")
    void deleteUserTest_User1NotExists() throws Exception {
        var requestBuilder = delete("/users/1");

        Mockito.when(userService.deleteUserById(1))
                .thenThrow(new UserServiceException("Попытка удалить пользователя с несуществующим id: 1"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с пользователями\":\"Попытка удалить пользователя с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей самого себя")
    void deleteFromUserFriendsTest_Delete() throws Exception {
        var requestBuilder = delete("/users/1/friends/1");

        Mockito.when(userFriendService.deletingFromUserFriends(1, 1))
                .thenThrow(new UserFriendServiceException("Нельзя удалить из друзей самого себя"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\":" +
                        "\"Нельзя удалить из друзей самого себя\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей у несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_User1NotExists() throws Exception {
        var requestBuilder = delete("/users/1/friends/2");

        Mockito.when(userFriendService.deletingFromUserFriends(1, 2))
                .thenThrow(new UserFriendServiceException(
                        "Попытка удалить друга для несуществующего пользователя с id: 1"));


        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\":" +
                        "\"Попытка удалить друга для несуществующего пользователя с id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей у несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_User2NotExists() throws Exception {
        var requestBuilder = delete("/users/1/friends/2");

        Mockito.when(userFriendService.deletingFromUserFriends(1, 2))
                .thenThrow(new UserFriendServiceException(
                        "Попытка удалить из друзей несуществующего пользователя с id: 2"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с друзьями\":" +
                        "\"Попытка удалить из друзей несуществующего пользователя с id: 2\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 удаляет из друзей у пользователя с id 1 пользователя с id 2")
    void deleteFromUserFriendsTest() throws Exception {
        var requestBuilder = delete("/users/1/friends/2");

        Mockito.when(userFriendService.deletingFromUserFriends(1, 1))
                .thenReturn(null);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                //content().string("Пользователь с id: 1 удалил из друзей пользователя с id: 2")
        );
    }

    @Test
    @DisplayName("GET /users/1/feed возвращает ленту событий пользователя 1")
    void getUserFeed() throws Exception {
        var requestBuilder = get("/users/1/feed");

        Mockito.when(eventService.getUserFeed(1)).thenReturn(List.of(
                Event.builder().eventId(1)
                        .timestamp(1712493175L)
                        .userId(1)
                        .eventType(EventType.FRIEND)
                        .operation(OperationType.ADD)
                        .entityId(2)
                        .build()
        ));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"eventId\":1," +
                        "\"timeStamp\":1712493175," +
                        "\"userId\":1," +
                        "\"eventType\":\"FRIEND\"," +
                        "\"operation\":\"ADD\"," +
                        "\"entityId\":2}]")
        );
    }
}