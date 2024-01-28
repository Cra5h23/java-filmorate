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
    private int id;

    /**
     * Электронная почта
     */
    private String email;

    /**
     * Логин пользователя
     */
    private String login;

    /**
     * Имя пользователя(для отображения)
     */
    private String name;

    /**
     * Дата рождения
     */
    private Instant birthday;

}
