package ru.yandex.practicum.filmorate.storage.user.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JdbcTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Метод getAllUser возвращает список всех пользователей")
    void getAllUserTest() {
        Collection<User> users = generateUsersList(3);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        users.forEach(userDbStorage::addUser);
        Collection<User> allUsers = userDbStorage.getAllUsers();
        Assertions.assertThat(allUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @Test
    @DisplayName("Метод addUser добавляет пользователя в базу данных")
    void addUserTest() {
        var users = generateUsersList(1);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        var user = users.stream().findFirst().map(userDbStorage::addUser).get();
        var userById = userDbStorage.getUserById(user.getId()).get();

        Assertions.assertThat(userById)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void testUpdateUser() {
        var users = generateUsersList(2);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        var addedUser = userDbStorage.addUser(users.get(0));
        var updateUser = users.get(1);
        updateUser.setId(addedUser.getId());
        userDbStorage.updateUser(updateUser);

        var userById = userDbStorage.getUserById(addedUser.getId()).get();

        Assertions.assertThat(userById)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updateUser);
    }

    @Test
    void getUserByIdTest() {
        var users = generateUsersList(1);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        User user = userDbStorage.addUser(users.get(0));
        int id = user.getId();

        Optional<User> userById = userDbStorage.getUserById(id);
        Assertions.assertThat(userById.get())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void deleteUser() {
        var users = generateUsersList(1);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        User user = userDbStorage.addUser(users.get(0));

        userDbStorage.deleteUser(user.getId());
        Optional<User> userById = userDbStorage.getUserById(user.getId());
        Assertions.assertThat(userById)
                .isNotPresent();
    }

    private List<User> generateUsersList(Integer count) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            var u = User.builder()
                    .id(i + 1)
                    .login("TestLogin" + i)
                    .name("TestName" + i)
                    .email(String.format("testEmail%d.com", i))
                    .birthday(LocalDate.now().minusYears(18).plusDays(i))
                    .build();
            users.add(u);
        }
        return users;
    }
}