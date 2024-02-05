package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserController userController;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        userController.getUserMap().clear();
    }

    @Test
    @DisplayName("GET localhost:8080/users возвращает коллекцию из двух пользователей")
    void getAllUserTest() throws Exception {
        var requestBuilder = get("http://localhost:8080/users");
        User user1 = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().minusYears(10))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user2 = User.builder()
                .email("testemail2@test.com")
                .birthday(LocalDate.now().minusYears(11))
                .login("TestLogin2")
                .name("TestName2")
                .build();
        List<User> users = List.of(this.userController.addNewUser(user1), this.userController.addNewUser(user2));
        String s = objectMapper.writeValueAsString(users);
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(s)
        );
    }

    @Test
    void addNewUserTestValid() throws Exception {
        String validUser = "{\"email\":\"validuser@test.com\"" +
                ",\"birthday\":\"2020-03-04\"" +
                ",\"login\":\"validUserLogin\"" +
                ",\"name\":\"validUserName\"}";

        var requestBuilder = post("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(validUser);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserName\"}"),
                jsonPath("$.id").exists()
        );
    }

    @Test
    void addNewUserTestNotValidEmail() throws Exception {
        String validUser = "{\"email\":\"@validUser\"" +
                ",\"birthday\":\"2020-03-04\"" +
                ",\"login\":\"validUserLogin\"" +
                ",\"name\":\"validUserName\"}";

        var requestBuilder = post("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(validUser);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void addNewUserTestValidLogin() throws Exception {
        String validUser = "{\"email\":\"validuser@test.com\"" +
                ",\"birthday\":\"2020-03-04\"" +
                ",\"login\":\"\"" +
                ",\"name\":\"validUserName\"}";

        var requestBuilder = post("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(validUser);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void addNewUserTestValidEmptyName() throws Exception {
        String validUser = "{\"email\":\"validuser@test.com\"" +
                ",\"birthday\":\"2020-03-04\"" +
                ",\"login\":\"validUserLogin\"" +
                ",\"name\":\"\"}";

        var requestBuilder = post("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(validUser);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"email\":\"validuser@test.com\"" +
                        ",\"birthday\":\"2020-03-04\"" +
                        ",\"login\":\"validUserLogin\"" +
                        ",\"name\":\"validUserLogin\"}"),
                jsonPath("$.id").exists()
        );
    }

    @Test
    void addNewUserTestNotValidBirthday() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().plusDays(1))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        String s = objectMapper.writeValueAsString(user);
        var requestBuilder = post("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateUserTestValid() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().minusYears(10))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user1 = userController.addNewUser(user);

        User userUpdate = user1.toBuilder()
                .email("update@test.com")
                .name("updateName")
                .login("updateLogin")
                .birthday(LocalDate.now().minusYears(2))
                .build();
        String s = objectMapper.writeValueAsString(userUpdate);

        var requestBuilder = put("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(s)
        );
    }

    @Test
    void updateUserTestNotValidEmail() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().minusYears(10))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user1 = userController.addNewUser(user);

        User userUpdate = user1.toBuilder()
                .email("update")
                .name("updateName")
                .login("updateLogin")
                .birthday(LocalDate.now().minusYears(2))
                .build();
        String s = objectMapper.writeValueAsString(userUpdate);

        var requestBuilder = put("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateUserTestNotValidLogin() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().minusYears(10))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user1 = userController.addNewUser(user);

        User userUpdate = user1.toBuilder()
                .email("update@test.com")
                .name("updateName")
                .login("")
                .birthday(LocalDate.now().minusYears(2))
                .build();
        String s = objectMapper.writeValueAsString(userUpdate);

        var requestBuilder = put("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void updateUserTestValidEmptyName() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.of(2000, 10, 12))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user1 = userController.addNewUser(user);

        User userUpdate = user1.toBuilder()
                .email("update@test.com")
                .name("")
                .login("updateLogin")
                .birthday(LocalDate.of(2001, 10, 12))
                .build();
        String s = objectMapper.writeValueAsString(userUpdate);

        var requestBuilder = put("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"email\":\"update@test.com\",\"login\":\"updateLogin\",\"name\":\"updateLogin\",\"birthday\":\"2001-10-12\"}"),
                jsonPath("$.id").value(user1.getId())
        );
    }

    @Test
    void updateUserTestNotValidBirthday() throws Exception {
        User user = User.builder()
                .email("testemail1@test.com")
                .birthday(LocalDate.now().minusYears(10))
                .login("TestLogin1")
                .name("TestName1")
                .build();
        User user1 = userController.addNewUser(user);

        User userUpdate = user1.toBuilder()
                .email("update@test.com")
                .name("updateName")
                .login("updateLogin")
                .birthday(LocalDate.now().plusDays(2))
                .build();
        String s = objectMapper.writeValueAsString(userUpdate);

        var requestBuilder = put("http://localhost:8080/users")
                .contentType(APPLICATION_JSON).content(s);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }
}