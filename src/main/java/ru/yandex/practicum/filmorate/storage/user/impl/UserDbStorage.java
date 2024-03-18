package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        var sql = "SELECT\n" +
                "a.*\n" +
                "FROM (SELECT\n" +
                "u.*,\n" +
                "string_agg(f.friend_id, ', ') friends\n" +
                "FROM USERS u\n" +
                "LEFT JOIN FRIENDS f ON u.user_id = f.user_id\n" +
                "GROUP BY u.user_id\n" +
                ") a\n";
        log.info("Создан запрос в базу данных на получение списка всех пользователей");
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User addUser(User user) {
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue());
        user.setFriends(Set.of());
        log.info("Создан запрос в базу данных на добавление пользователя {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        var sql = "update users set email=?, login=?, name=?, birthday=? where user_id=?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        //log.info("Создан запрос в базу данных на обновление пользователя");
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        var sql = "SELECT\n" +
                "a.*\n" +
                "FROM (SELECT\n" +
                "u.*,\n" +
                "string_agg(f.friend_id, ', ') friends\n" +
                "FROM USERS u\n" +
                "LEFT JOIN FRIENDS f ON u.user_id = f.user_id\n" +
                "GROUP BY u.user_id\n" +
                ") a\n" +
                "WHERE user_id = ?\n";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::makeUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }


        //return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::makeUser, id));
    }

    @Override
    public void deleteUser(int id) {
        var sql = "delete from friends where user_id=?;" +
                "delete from likes where user_id=?;" +
                "delete from users where user_id=?;";
        jdbcTemplate.update(sql, id, id, id);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(rs.getString("friends") != null ? Arrays.stream(
                                rs.getString("friends").split(", "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet()) : Set.of())
                .build();
    }

    public Map<String, Object> toMap(User user) {
        return Map.of("user_id", user.getId(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday());
    }
}