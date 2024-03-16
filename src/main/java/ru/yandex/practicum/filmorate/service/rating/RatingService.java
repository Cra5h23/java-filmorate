package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

public interface RatingService {
    Collection<Rating> getAllRatings();
    Rating getRatingById(Integer ratingId);
}
