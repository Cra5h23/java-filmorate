package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Optional<Review> getById(Integer id);

    List<Review> getByParams(Integer filmId, Integer count);

    Optional<Review> getByFilmIdAndUserId(Integer filmId, Integer userId);

    Integer create(Review review);

    void update(Review review);

    void deleteById(Integer id);
}
