package ru.yandex.practicum.filmorate.dao.review;

import ru.yandex.practicum.filmorate.model.ReviewRating;
import java.util.Optional;

public interface ReviewRatingDao {
    Optional<ReviewRating> getByParam(Integer id, Integer userId);

    void create(ReviewRating reviewRating);

    void update(ReviewRating reviewRating);

    void delete(ReviewRating reviewRating);
}
