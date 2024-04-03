package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.ReviewRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRatingMapper implements RowMapper<ReviewRating> {
    @Override
    public ReviewRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReviewRating reviewRating = new ReviewRating();
        reviewRating.setReviewId(rs.getInt("review_id"));
        reviewRating.setUserId(rs.getInt("user_id"));
        reviewRating.setIsLike(rs.getBoolean("islike"));

        return reviewRating;
    }
}
