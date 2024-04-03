package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.util.user.UserUtil;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        var sql = "select * from users";

        log.info("Создан запрос в базу данных на получение списка всех пользователей");
        return jdbcTemplate.query(sql, UserUtil::makeUser);
    }

    @Override
    public User addUser(User user) {
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        user.setId(simpleJdbcInsert.executeAndReturnKey(UserUtil.toMap(user)).intValue());
        log.info("Создан запрос в базу данных на добавление пользователя {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        var sql = "update users set email=?, login=?, name=?, birthday=? where user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        var sql = "select * from users where user_id=?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, UserUtil::makeUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(int id) {
        var sql = "delete from friends where user_id=?;" +
                "delete from likes where user_id=?;" +
                "delete from users where user_id=?;";
        jdbcTemplate.update(sql, id, id, id);
    }
}
