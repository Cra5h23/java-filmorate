package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Review;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Маппер для {@link Review}.
 */
public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setFilmId(rs.getInt("film_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("ispositive"));
        review.setUseful(rs.getInt("useful"));

        return review;
    }
}
