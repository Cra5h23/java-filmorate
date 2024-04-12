package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Review.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    /**
     * Идентификатор.
     */
    Integer reviewId;

    /**
     * Идентификатор фильма, на который создан отзыв
     */
    @NotNull(message = "Идентификатор фильма (filmId) не должен быть пустым")
    Integer filmId;

    /**
     * Идентификатор пользователя создавшего отзыв.
     */
    @NotNull(message = "Идентификатор пользователя (userId) не должен быть пустым")
    Integer userId;


    /**
     * Текст отзыва.
     */
    @NotBlank(message = "Текст отзыва (content) не должен быть пустым")
    String content;

    /**
     * Признак типа отзыва: true = положительный, false = негативный.
     */
    @NotNull(message = "Тип отзыва (isPositive) не должен быть пустым.")
    Boolean isPositive;

    /**
     * Рейтинг полезности.
     */
    Integer useful;
}
