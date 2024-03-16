package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum FilmSort {
    POPULAR_FILMS_DESC("SELECT " +
            "f.*, \n" +
            "STRING_AGG(fg.GENRE_ID, ', ') genres, \n" +
            "STRING_AGG(l.USER_ID, ', ') likes \n" +
            "FROM FILMS f\n" +
            "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID \n" +
            "LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID \n" +
            "GROUP BY f.FILM_ID \n" +
            "ORDER BY count(l.user_id) DESC\n" +
            "limit ?", comparingInt(f -> (f.getLikes().size() * -1))),
    POPULAR_FILMS_ASC("SELECT " +
            "f.*, \n" +
            "STRING_AGG(fg.GENRE_ID, ', ') genres, \n" +
            "STRING_AGG(l.USER_ID, ', ') likes \n" +
            "FROM FILMS f\n" +
            "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID \n" +
            "LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID \n" +
            "GROUP BY f.FILM_ID \n" +
            "ORDER BY count(l.user_id) \n" +
            "limit ?", comparingInt(f -> (f.getLikes().size())));

    private String sql;
    private Comparator<Film> comparator;
}
