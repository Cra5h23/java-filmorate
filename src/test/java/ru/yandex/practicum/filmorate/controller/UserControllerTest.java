package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InMemoryUserStorage userStorage;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("GET /users возвращает коллекцию из двух пользователей ")
    void getAllUserTest() throws Exception {
        var requestBuilder = get("/users");
        userStorage.getUserMap().putAll(generatorUserMap(2));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().contentType(APPLICATION_JSON)
                , content().json("[{\"id\":1" +
                        ",\"email\":\"testEmail@test.com1\"" +
                        ",\"login\":\"testLogin1\"" +
                        ",\"name\":\"testName1\"" +
                        ",\"birthday\":\"1990-01-02\"" +
                        ",\"friends\":[]}" +
                        ",{\"id\":2" +
                        ",\"email\":\"testEmail@test.com2\"" +
                        ",\"login\":\"testLogin2\"" +
                        ",\"name\":\"testName2\"" +
                        ",\"birthday\":\"1990-01-03\"" +
                        ",\"friends\":[]}]")
        );
    }

    @Test
    @DisplayName("POST /users создаёт нового пользователя и возвращает статус 201")
    void addNewUserTestValid() throws Exception {
        var requestBuilder = post("/users")
                .contentType(APPLICATION_JSON).content("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}");

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
                status().isBadRequest()
                , content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"must be a well-formed email address\"}")
                , jsonPath("$.timestamp").exists()
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
                status().isBadRequest()
                , content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Логин пользователя не должен быть пустым\"}")
                , jsonPath("$.timestamp").exists()
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
                status().isBadRequest()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка ввода данных\":\"Дата рождения не должна быть в будущем\"}")
                ,jsonPath("$.timestamp").exists()
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
        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserNameUpdate\"" +
                        ",\"birthday\":\"2020-03-04\"," +
                        "\"friends\":[]}")
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

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
                , content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"must be a well-formed email address\"}")
                , jsonPath("$.timestamp").exists()
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
        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
                , content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Логин пользователя не должен быть пустым\"}")
                , jsonPath("$.timestamp").exists()
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
        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"id\":1" +
                        ",\"email\":\"validuserUpdate@test.com\"" +
                        ",\"login\":\"validUserLoginUpdate\"" +
                        ",\"name\":\"validUserLoginUpdate\"" +
                        ",\"birthday\":\"2020-03-04\"," +
                        "\"friends\":[]}")
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
                        ",\"name\":\"validUserNameUpdate\"}",LocalDate.now().plusDays(1)));
        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
                , content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Дата рождения не должна быть в будущем\"}")
                , jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 1 добавляет в друзья пользователя 2")
    void addUserToFriendsTest_User1AddToFriendsUser2() throws Exception{
        var requestBuilder= put("/users/1/friends/2");

        userStorage.getUserMap().putAll(generatorUserMap(2));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().string("Пользователь с id: 1 добавил в друзья пользователя с id: 2")
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 2 не существует")
    void addUserToFriendsTest_User1NotExists() throws Exception{
        var requestBuilder= put("/users/1/friends/2");
        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().json("{\"Ошибка добавления пользователя в друзья\"" +
                        ":\"Попытка добавить в друзья несуществующего пользователя с id: 2\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /users/1/friends/2 пользователь 1 не существует")
    void addUserToFriendsTest_User2NotExists() throws Exception{
        var requestBuilder= put("/users/1/friends/2");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().json("{\"Ошибка добавления пользователя в друзья\"" +
                        ":\"Попытка добавить друга для несуществующего пользователя с id: 1\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1 возвращает пользователя с id 1")
    void getUserByIdTest_GetUser1() throws Exception{
        var requestBuilder= get("/users/1");

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"id\":1" +
                        ",\"email\":\"testEmail@test.com1\"" +
                        ",\"login\":\"testLogin1\"" +
                        ",\"name\":\"testName1\"" +
                        ",\"birthday\":\"1990-01-02\"" +
                        ",\"friends\":[]}")
        );
    }

    @Test
    @DisplayName("GET /users/1 не возвращает не существующего пользователя с id 1")
    void getUserByIdTest_GetUser1NotExists() throws Exception{
        var requestBuilder= get("/users/1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка получения пользователя\":\"Пользователь с id: 1 не существует\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1 удаляет пользователя с id 1")
    void deleteFromUserFriendsTest_DeleteUser1() throws Exception{
        var requestBuilder= delete("/users/1");

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().string("Удалён пользователь с id: 1")

        );
    }

    @Test
    @DisplayName("DELETE /users/1 не удаляет несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_DeleteUser1NotExists() throws Exception{
        var requestBuilder= delete("/users/1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка получения пользователя\":\"Пользователь с id: 1 не существует\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends возвращает список друзей для пользователя с id 1")
    void getUsersFriendsTest_ReturnListFriends() throws Exception {
        var requestBuilder= get("/users/1/friends");

        userStorage.getUserMap().putAll(generatorUserMap(3));
        userService.addingUserAsFriend(1,2);
        userService.addingUserAsFriend(1,3);
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("[{\"id\":2" +
                        ",\"email\":\"testEmail@test.com2\"" +
                        ",\"login\":\"testLogin2\"" +
                        ",\"name\":\"testName2\"" +
                        ",\"birthday\":\"1990-01-03\"" +
                        ",\"friends\":[1]}," +
                        "{\"id\":3" +
                        ",\"email\":\"testEmail@test.com3\"" +
                        ",\"login\":\"testLogin3\"" +
                        ",\"name\":\"testName3\"" +
                        ",\"birthday\":\"1990-01-04\"" +
                        ",\"friends\":[1]}]")
        );
    }

    @Test
    @DisplayName("GET /users/1/friends не возвращает список друзей для несуществующего пользователя с id 1")
    void getUsersFriendsTest_User1NotExists() throws Exception {
        var requestBuilder= get("/users/1/friends");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\"" +
                        ":\"Попытка получить список друзей для несуществующего пользователя с id: 1\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 возвращает список общих друзей для пользователя с id 1 и пользователя с id 2")
    void getUsersCommonFriendsTest_ReturnsListCommonFriends() throws Exception {
        var requestBuilder= get("/users/1/friends/common/2");

        userStorage.getUserMap().putAll(generatorUserMap(5));
        userService.addingUserAsFriend(1,3);
        userService.addingUserAsFriend(1,4);
        userService.addingUserAsFriend(1,5);
        userService.addingUserAsFriend(2,4);
        userService.addingUserAsFriend(2,5);
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("[{\"id\":4" +
                        ",\"email\":\"testEmail@test.com4\"" +
                        ",\"login\":\"testLogin4\"" +
                        ",\"name\":\"testName4\"" +
                        ",\"birthday\":\"1990-01-05\"" +
                        ",\"friends\":[1,2]}" +
                        ",{\"id\":5" +
                        ",\"email\":\"testEmail@test.com5\"" +
                        ",\"login\":\"testLogin5\"" +
                        ",\"name\":\"testName5\"" +
                        ",\"birthday\":\"1990-01-06\"" +
                        ",\"friends\":[1,2]}]")
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 не возвращает список общих друзей для несуществующего пользователя с id 1")
    void getUsersCommonFriendsTest_User1NotExists() throws Exception {
        var requestBuilder= get("/users/1/friends/common/2");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\"" +
                        ":\"Попытка получить список общих друзей для несуществующего пользователя с id: 1\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /users/1/friends/common/2 не возвращает список общих друзей для пользователя с id 1 " +
            " и не существующего пользователя с id 2")
    void getUsersCommonFriendsTest_User2NotExists() throws Exception {
        var requestBuilder= get("/users/1/friends/common/2");

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\":\"Попытка получить список общих друзей несуществующего пользователя с id: 2\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1 удаляет пользователя с id 1")
    void deleteUserTest() throws Exception {
        var requestBuilder= delete("/users/1");

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().string("Удалён пользователь с id: 1")
        );
    }

    @Test
    @DisplayName("DELETE /users/1 не удаляет несуществующего пользователя с id 1")
    void deleteUserTest_User1NotExists() throws Exception {
        var requestBuilder= delete("/users/1");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка получения пользователя\":\"Пользователь с id: 1 не существует\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей самого себя")
    void deleteFromUserFriendsTest_Delete() throws Exception{
        var requestBuilder = delete("/users/1/friends/1");

        userStorage.getUserMap().putAll(generatorUserMap(1));
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\":\"Нельзя удалить из друзей самого себя\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей у несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_User1NotExists() throws Exception{
        var requestBuilder = delete("/users/1/friends/2");

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\":\"Попытка удалить друга для несуществующего пользователя с id: 1\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 не удаляет из друзей у несуществующего пользователя с id 1")
    void deleteFromUserFriendsTest_User2NotExists() throws Exception{
        var requestBuilder = delete("/users/1/friends/2");
        userStorage.getUserMap().putAll(generatorUserMap(1));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                ,content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка добавления пользователя в друзья\":\"Попытка удалить из друзей несуществующего пользователя с id: 2\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /users/1/friends/1 удаляет из друзей у пользователя с id 1 пользователя с id 2")
    void deleteFromUserFriendsTest() throws Exception{
        var requestBuilder = delete("/users/1/friends/2");
        userStorage.getUserMap().putAll(generatorUserMap(2));
        userService.addingUserAsFriend(1,2);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().string("Пользователь с id: 1 удалил из друзей пользователя с id: 2")
        );
    }

    private Map<Integer, User> generatorUserMap(int userQuantity) {
        Map<Integer, User> userMap = new HashMap<>();
        for (int i = 1; i <= userQuantity; i++) {
            userMap.put(i, new User(i, "testEmail@test.com" + i, "testLogin" + i, "testName" + i,
                    LocalDate.of(1990, 1, 1).plusDays(i)));
        }
        return userMap;
    }

}