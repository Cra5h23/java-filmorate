package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ReviewRating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRating {
    /**
     * Идентификатор отзыва.
     */
    private Integer reviewId;

    /**
     * Идентификатор пользователя.
     */
    private Integer userId;

    /**
     * Признак оценки: true = лайк, false = дизлайк.
     */
    private Integer rating;
}
