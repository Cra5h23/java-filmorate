package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

/**
 * Film.
 *
 * @author Nikolay Radzivon
 */
@Data
@Builder
public class Film {
    /**
     * Индификатор фильма
     */
    @Builder.Default
    private int id = 0;

    /**
     * Имя фильма
     */
    private String name;
    /**
     * Описание фильма
     */
    private String description;
    /**
     * Дата релиза фильма
     */
    private LocalDate releaseDate;
    /**
     * Продолжительнность фильма
     */
    private int duration;
}
