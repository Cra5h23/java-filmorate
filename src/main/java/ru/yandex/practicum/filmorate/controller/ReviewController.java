package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Получение всех отзывов по идентификатору фильма, если фильм не указан то все.
     * Если кол-во не указано, то 10.
     */
    @GetMapping
    public ResponseEntity<Collection<Review>> getByParams(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = "10") @Min(1) Integer count
    ) {
        log.info("GET /reviews?filmId={}&count={}", filmId, count);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.getByParams(filmId, count));
    }



    /**
     * Получение отзыва по идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Integer id) {
        log.info("GET /reviews/{}", id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.getReviewById(id));
    }

    /**
     * Добавление нового отзыва.
     */
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        log.info("POST /reviews");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.createReview(review));
    }

    /**
     * Редактирование отзыва.
     */
    @PutMapping
    public ResponseEntity<Review> updateReview(@Valid @RequestBody Review review) {
        log.info("PUT /reviews");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.updateReview(review));
    }

    /**
     * Лайк отзыву.
     */
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> likeToReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("PUT /reviews/{}/like/{}", id, userId);

        return ResponseEntity.ok(reviewService.likeToReview(id, userId));
    }

    /**
     * Дизлайк отзыву.
     */
    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<?> dislikeToReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("PUT /reviews/{}/dislike/{}", id, userId);

        return ResponseEntity.ok(reviewService.dislikeToReview(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        log.info("DELETE /reviews/{}", id);

        return ResponseEntity.ok(reviewService.deleteReviewById(id));
    }

    /**
     * Удаление лайка отзыва.
     */
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeFromReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("DELETE /reviews/{}/like/{}", id, userId);

        return ResponseEntity.ok(reviewService.deleteLikeFromReview(id, userId));
    }


    /**
     * Удаление дизлайка отзыва.
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<?> deleteDislikeFromReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("DELETE /reviews/{}/dislike/{}", id, userId);

        return ResponseEntity.ok(reviewService.deleteDislikeFromReview(id, userId));
    }

}
