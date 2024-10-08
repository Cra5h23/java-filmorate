package ru.yandex.practicum.filmorate.service.review.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventDao;
import ru.yandex.practicum.filmorate.dao.review.ReviewDao;
import ru.yandex.practicum.filmorate.dao.review.ReviewRatingDao;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewRating;
import ru.yandex.practicum.filmorate.service.review.ReviewService;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;

    private final FilmDao filmDao;

    private final UserDao userDao;

    private final ReviewRatingDao reviewRatingDao;
    private final EventDao eventDao;

    static final Integer LIKE_RATING = 1;

    static final Integer DISLIKE_RATING = -1;

    @Override
    public Review getReviewById(Integer id) {
        log.info("Запрошен отзыв с id {}", id);
        return reviewDao.getById(id).orElseThrow(() -> new NotFoundException(
                String.format("Запрашиваемый отзыв с reviewId %d не существует", id)));
    }

    @Override
    public List<Review> getByParams(Integer filmId, Integer count) {
        return reviewDao.getByParams(filmId, count);
    }

    @Override
    public Review createReview(Review review) {
        checkExistsFilmById(review.getFilmId());
        checkExistsUserById(review.getUserId());
        checkExistsReviewByFilmIdAndUserId(review.getFilmId(), review.getUserId());

        Integer reviewId = reviewDao.create(review);
        eventDao.createAddReviewEvent(reviewId, review.getUserId());

        return getReviewById(reviewId);
    }

    @Override
    public Review updateReview(Review review) {
        Integer id = review.getReviewId();
        Review dbReview = checkExistsReviewById(id);

        reviewDao.update(review);
        eventDao.createUpdateReviewEvent(id,dbReview.getUserId());

        return reviewDao.getById(id).orElse(null);
    }

    @Override
    public String deleteReviewById(Integer id) {
        Review review = checkExistsReviewById(id);
        reviewDao.deleteById(id);

        eventDao.createDeleteReviewEvent(id,review.getUserId());

        return String.format("Обзор с reviewId %d удалён", id);
    }

    @Override
    public String likeToReview(Integer id, Integer userId) {
        checkExistsReviewById(id);
        checkExistsUserById(userId);

        Optional<ReviewRating> ratingOptional = reviewRatingDao.getByParam(id, userId);

        if (ratingOptional.isPresent()) {
            ReviewRating reviewRating = ratingOptional.get();
            Integer rating = reviewRating.getRating();

            if (Objects.equals(rating, LIKE_RATING)) {
                return String.format(
                        "Пользователь с userId %d уже поставил лайк отзыву c reviewId %d", userId, id
                );
            } else {
                reviewRating.setRating(DISLIKE_RATING);
                reviewRatingDao.update(reviewRating);

                return String.format(
                        "Пользователь с userId %d сменил дизлайк на лайк отзыву c reviewId %d", userId, id
                );
            }
        }

        ReviewRating newReviewRating = ReviewRating.builder()
                .reviewId(id)
                .userId(userId)
                .rating(LIKE_RATING)
                .build();
        reviewRatingDao.create(newReviewRating);

        return String.format(
                "Пользователь с userId %d поставил лайк отзыву c reviewId %d", userId, id
        );
    }

    @Override
    public String dislikeToReview(Integer id, Integer userId) {
        checkExistsReviewById(id);
        checkExistsUserById(userId);

        Optional<ReviewRating> ratingOptional = reviewRatingDao.getByParam(id, userId);

        if (ratingOptional.isPresent()) {
            ReviewRating reviewRating = ratingOptional.get();
            Integer rating = reviewRating.getRating();

            if (Objects.equals(rating, DISLIKE_RATING)) {
                return String.format(
                        "Пользователь с userId %d уже поставил дизлайк отзыву c reviewId %d", userId, id
                );
            } else {
                reviewRating.setRating(DISLIKE_RATING);
                reviewRatingDao.update(reviewRating);

                return String.format(
                        "Пользователь с userId %d сменил лайк на дизлайк отзыву c reviewId %d", userId, id
                );
            }
        }

        ReviewRating newReviewRating = ReviewRating.builder()
                .reviewId(id)
                .userId(userId)
                .rating(DISLIKE_RATING)
                .build();
        reviewRatingDao.create(newReviewRating);

        return String.format(
                "Пользователь с userId %d поставил дизлайк отзыву c reviewId %d", userId, id
        );
    }

    @Override
    public String deleteLikeFromReview(Integer id, Integer userId) {
        checkExistsReviewById(id);
        checkExistsUserById(userId);

        Optional<ReviewRating> ratingOptional = reviewRatingDao.getByParam(id, userId);

        if (ratingOptional.isPresent() && Objects.equals(ratingOptional.get().getRating(), LIKE_RATING)) {
            reviewRatingDao.delete(ratingOptional.get());

            return String.format(
                    "Лайк от пользователя с userId %d удалён", userId
            );
        }

        return String.format(
                "У отзыва c reviewId %d отсутствует лайк от пользователя с userId %d", id, userId
        );
    }

    @Override
    public String deleteDislikeFromReview(Integer id, Integer userId) {
        checkExistsReviewById(id);
        checkExistsUserById(userId);

        Optional<ReviewRating> ratingOptional = reviewRatingDao.getByParam(id, userId);

        if (ratingOptional.isPresent() && Objects.equals(ratingOptional.get().getRating(), DISLIKE_RATING)) {
            reviewRatingDao.delete(ratingOptional.get());

            return String.format(
                    "Дизлайк от пользователя с userId %d удалён", userId
            );
        }

        return String.format(
                "У отзыва c reviewId %d отсутствует Дизлайк от пользователя с userId %d", id, userId
        );
    }

    private void checkExistsFilmById(Integer id) {
        filmDao.getFilmById(id).orElseThrow(() -> new NotFoundException(
                String.format("Фильма с filmId %d не существует", id)));
    }

    private void checkExistsUserById(Integer id) {
        userDao.getUserById(id).orElseThrow(() -> new NotFoundException(
                String.format("Пользователя с userId %d не существует", id)));
    }

    private Review checkExistsReviewById(Integer id) {
        return reviewDao.getById(id).orElseThrow(() -> new NotFoundException(
                String.format("Отзыва с reviewId %d не существует", id)));
    }

    private void checkExistsReviewByFilmIdAndUserId(Integer filmId, Integer userId) {
        Optional<Review> reviewOptional = reviewDao.getByFilmIdAndUserId(filmId, userId);

        if (reviewOptional.isPresent()) {
            throw new BadRequestException(
                    String.format("Отзыв по фильму с filmId %d от пользователя " +
                            "c userId %d уже существует", filmId, userId)
            );
        }
    }

}
