package ru.yandex.practicum.filmorate.dao.review.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.review.ReviewRatingDao;
import ru.yandex.practicum.filmorate.mapper.ReviewRatingMapper;
import ru.yandex.practicum.filmorate.model.ReviewRating;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewRatingDaoImpl implements ReviewRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<ReviewRating> getByParam(Integer id, Integer userId) {
        Object[] params = new Object[]{id, userId};
        String sql = "select review_id, user_id, islike from reviewratings " +
                "where review_id = ? and user_id = ?";
        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        return jdbcTemplate.query(sql, new ReviewRatingMapper(), params).stream().findAny();
    }

    @Override
    public void create(ReviewRating reviewRating) {
        Object[] params = new Object[]{
                reviewRating.getReviewId(),
                reviewRating.getUserId(),
                reviewRating.getIsLike()
        };
        String sql = "insert into reviewratings(review_id, user_id, islike) " +
                "values (?, ?, ?)";

        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void update(ReviewRating reviewRating) {
        Object[] params = new Object[]{
                reviewRating.getIsLike(),
                reviewRating.getReviewId(),
                reviewRating.getUserId()
        };
        String sql = "update reviewratings " +
                "set islike = ? " +
                "where review_id = ? and user_id = ?";

        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(ReviewRating reviewRating) {
        Object[] params = new Object[]{
                reviewRating.getReviewId(),
                reviewRating.getUserId(),
                reviewRating.getIsLike()
        };
        String sql = "delete from reviewratings " +
                "where review_id = ? and user_id = ? and islike = ?";

        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);
        jdbcTemplate.update(sql, params);
    }
}
