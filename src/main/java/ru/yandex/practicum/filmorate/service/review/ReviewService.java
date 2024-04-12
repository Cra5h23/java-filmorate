package ru.yandex.practicum.filmorate.service.review;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;

public interface ReviewService {
    Review getReviewById(Integer id);

    List<Review> getByParams(Integer filmId, Integer count);

    Review createReview(Review review);

    Review updateReview(Review review);

    String deleteReviewById(Integer id);

    String likeToReview(Integer id, Integer userId);

    String dislikeToReview(Integer id, Integer userId);

    String deleteLikeFromReview(Integer id, Integer userId);

    String deleteDislikeFromReview(Integer id, Integer userId);
}
