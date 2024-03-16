package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addingUserAsFriend(Integer userId, Integer friendId) {
        var sql = "insert into friends(user_id, friend_id) values(?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deletingFromUserFriends(Integer userId, Integer deletingFriendId) {
        var sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, deletingFriendId);
    }

    @Override
    public Collection<?> getUserFriends(Integer userId) {
        var sql = "select u.* from friends f left join users u on f.friend_id = u.user_id where f.user_id =?";
        //jdbcTemplate.queryForObject(sql, this::makeUser,userId);
        try {
            return jdbcTemplate.query(sql, this::makeUser, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId) {
        var sql = "select u.*\n" +
                "from friends f\n" +
                "join friends f2 on f.friend_id = f2.friend_id and f2.user_id=?\n" +
                "join users u on f.friend_id = u.user_id\n" +
                "where f.user_id=?;";
        try {
            return jdbcTemplate.query(sql, this::makeUser, otherUserId, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
