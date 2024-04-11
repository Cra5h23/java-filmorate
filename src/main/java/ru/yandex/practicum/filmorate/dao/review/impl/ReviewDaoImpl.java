package ru.yandex.practicum.filmorate.dao.review.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.review.ReviewDao;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewDaoImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    private final String mainGetFields = "select " +
            "r.review_id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.ispositive, " +
            "sum(coalesce(rr.rating,0)) as useful ";

    private final String mainGetFrom = "from reviews as r " +
            "left join reviewratings as rr on r.review_id = rr.review_id ";

    private final String mainGetGroup = "group by " +
            "r.review_id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.ispositive ";

    @Override
    public Optional<Review> getById(Integer id) {
        Object[] params = new Object[]{id};
        String sql = mainGetFields +
                mainGetFrom +
                "where r.review_id = ? " +
                mainGetGroup;
        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        return jdbcTemplate.query(sql, new ReviewMapper(), params).stream().findAny();
    }

    @Override
    public List<Review> getByParams(Integer filmId, Integer count) {
        Object[] params;
        String whereStr = "";
        if (filmId != null) {
            whereStr = "where r.film_id = ? ";
            params = new Object[]{filmId, count};
        } else {
            params = new Object[]{count};
        }

        String sql = mainGetFields +
                mainGetFrom +
                whereStr +
                mainGetGroup +
                "order by useful desc, r.review_id " +
                "limit ?";
        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        return jdbcTemplate.query(sql, new ReviewMapper(), params);
    }

    @Override
    public Optional<Review> getByFilmIdAndUserId(Integer filmId, Integer userId) {
        Object[] params = new Object[]{filmId, userId};
        String sql = "select " +
                "r.review_id, " +
                "r.film_id, " +
                "r.user_id, " +
                "r.content, " +
                "r.ispositive, " +
                "0 as useful " +
                "from reviews as r " +
                "where r.film_id = ? and r.user_id = ? ";
        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        return jdbcTemplate.query(sql, new ReviewMapper(), params).stream().findAny();
    }

    @Override
    public Integer create(Review review) {
        Object[] params = new Object[]{
                review.getFilmId(),
                review.getUserId(),
                review.getContent(),
                review.getIsPositive()
        };

        String sql = "insert into reviews(film_id, user_id, content, ispositive) " +
                "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.debug("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"review_id"});
            ps.setInt(1, (Integer) params[0]);
            ps.setInt(2, (Integer) params[1]);
            ps.setString(3, (String) params[2]);
            ps.setBoolean(4, (Boolean) params[3]);
            return ps;
        }, keyHolder);

        return (Integer) (keyHolder.getKey());
    }

    @Override
    public void update(Review review) {
        Integer id = review.getReviewId();
        Object[] params = new Object[]{
                review.getContent(),
                review.getIsPositive(),
                id
        };
        String sql = "update reviews " +
                "set " +
                "content = ?, " +
                "ispositive = ? " +
                "where review_id = ?";

        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteById(Integer id) {
        Object[] params = new Object[]{id};
        String sql = "delete from reviews where review_id = ?";

        jdbcTemplate.update(sql, params);
    }
}
