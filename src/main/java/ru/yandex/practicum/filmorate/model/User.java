package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;

/**
 * Класс User
 *
 * @author Nikolay Radzivon
 */
@Data
public class User {
    /**
     * Индификатор пользователя
     */
    private final int id;

    /**
     * Электронная почта
     */
    private final String email;

    /**
     * Логин пользователя
     */
    private final String login;

    /**
     * Имя пользователя(для отображения)
     */
    private final String name;

    /**
     * Дата рождения
     */
    private final Instant birthday;

}
