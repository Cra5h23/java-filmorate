package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Like.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    Integer filmId;

    Integer userId;
}
