package ru.yandex.practicum.filmorate.dao.like.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;

@RequiredArgsConstructor
@Slf4j
@Component
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveLike(Integer filmId, Integer userId) {
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var param = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        var sql = "merge into likes(film_id, user_id) values (:film_id, :user_id)";

        namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        var sql = "delete from likes l where l.film_id=? and l.user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
