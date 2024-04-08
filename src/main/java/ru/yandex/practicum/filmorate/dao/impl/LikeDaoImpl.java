package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.model.Like;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveLike(Integer filmId, Integer userId) {
        var sql = "insert into likes(film_id, user_id) values(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        var sql = "delete from likes l where l.film_id=? and l.user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Like> getAllFilmLikes() {
        String sql = "select film_id, user_id from likes";
        return jdbcTemplate.query(sql, new LikeMapper());
    }
}
