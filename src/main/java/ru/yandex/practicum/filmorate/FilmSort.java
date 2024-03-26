package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum FilmSort {
    POPULAR_FILMS_DESC("SELECT " +
            "f.*, \n" +
            "r.rating_name \n" +
            "FROM FILMS f\n" +
            "LEFT JOIN ratings r on f.rating_id=r.rating_id \n" +
            "LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID \n" +
            "GROUP BY f.FILM_ID \n" +
            "ORDER BY count(l.user_id) DESC\n" +
            "limit ?");

    private String sql;
}
