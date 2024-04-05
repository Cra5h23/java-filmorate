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
            "r.rating_name, \n" +
            "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
            "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
            "FROM FILMS f\n" +
            "LEFT JOIN ratings r on f.rating_id=r.rating_id \n" +
            "LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID \n" +
            "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
            "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
            "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
            "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
            "GROUP BY f.FILM_ID \n" +
            "ORDER BY count(l.user_id) DESC\n" +
            "limit ?"),

    FILMS_BY_DIRECTOR_SORT_YEAR("SELECT f.*,\n" +
            "r.RATING_NAME,\n" +
            "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
            "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
            "FROM FILMS f\n" +
            "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
            "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
            "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
            "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
            "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
            "WHERE d.DIRECTOR_ID = ?\n" +
            "GROUP BY d.DIRECTOR_ID, F.FILM_ID\n" +
            "ORDER BY f.RELEASE_DATE \n"),

    FILMS_BY_DIRECTOR_SORT_LIKES("SELECT\n" +
            "f.*,\n" +
            "r.RATING_NAME,\n" +
            "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
            "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
            "FROM FILMS f\n" +
            "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
            "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
            "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
            "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
            "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
            "LEFT JOIN LIKES l ON l.FILM_ID = f.FILM_ID\n" +
            "WHERE d.DIRECTOR_ID = ?\n" +
            "GROUP BY d.DIRECTOR_ID , F.FILM_ID\n" +
            "ORDER BY COUNT(DISTINCT l.USER_ID) DESC\n");

    private String sql;
}
