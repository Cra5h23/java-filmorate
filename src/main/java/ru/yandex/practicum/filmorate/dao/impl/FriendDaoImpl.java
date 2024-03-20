package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.util.UserUtil;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addingUserAsFriend(Integer userId, Integer friendId) {
        var sql = "insert into friends(user_id, friend_id, status) values(?, ?, ?);" +
                "insert into friends(user_id, friend_id, status) values(?,?, ?);";
        jdbcTemplate.update(sql, userId, friendId, true, friendId, userId, false);
    }

    @Override
    public void deletingFromUserFriends(Integer userId, Integer deletingFriendId) {
        var sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, deletingFriendId);
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        var sql = "select u.* from friends f left join users u on f.friend_id=u.user_id where f.user_id=? and f.status=true";
        try {
            return jdbcTemplate.query(sql, UserUtil::makeUser, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId) {
        var sql = "select u.*\n" +
                "from friends f\n" +
                "join friends f2 on f.friend_id = f2.friend_id and f2.user_id=? and f2.status= true\n" +
                "join users u on f.friend_id = u.user_id\n" +
                "where f.user_id=? and f.status= true;";
        try {
            return jdbcTemplate.query(sql, UserUtil::makeUser, otherUserId, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public String confirmFriend(Integer userId, Integer friendId, boolean confirm) {
        var sql = "update friends set status=? where user_id=? and friend_id=?";
        int update = jdbcTemplate.update(sql, confirm, userId, friendId);
        if (update == 1) {
            return confirm ?
                    String.format("Пользователь с id %d подтвердил дружбу с пользователем с id %d", userId, friendId) :
                    String.format("Пользователь с id %d не подтвердил дружбу с пользователем с id %d", userId, friendId);
        } else {
            return String.format(
                    "Пользователь с id %d не отправлял запрос на добавление в друзья пользователю с id %d", friendId, userId);
        }
    }
}