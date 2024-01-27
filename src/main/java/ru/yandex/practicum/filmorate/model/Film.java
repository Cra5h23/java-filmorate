package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Film.
 *
 * @author Nikolay Radzivon
 */
@Data
public class Film {
    /**
     * Индификатор фильма
     */
    private final int id;
    /**
     * Имя фильма
     */
    private final String name;
    /**
     * Описание фильма
     */
    private final String description;
    /**
     * Дата релиза фильма
     */
    private final Instant releaseDate;
    /**
     * Продолжительнность фильма
     */
    private final int duration;

}
