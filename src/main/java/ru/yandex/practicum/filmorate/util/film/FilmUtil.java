package ru.yandex.practicum.filmorate.util.film;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nikolay Radzivon
 */
public class FilmUtil {
    public static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(rs.getInt("rating_id") != 0 && rs.getString("rating_name") != null ?
                        new Rating(rs.getInt("rating_id"), rs.getString("rating_name"))
                        : new Rating())
                .genres(makeGenreList(rs))
                .directors(makeDirectorList(rs))
                .build();
    }

    public static MapSqlParameterSource toMap(Film film) {
        return new MapSqlParameterSource()
                .addValue("film_id", film.getId())
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("rating_id", film.getMpa().getId());
    }

    private static List<Genre> makeGenreList(ResultSet rs) throws SQLException {
        var genres = rs.getString("genres");

        return Arrays.stream(genres.substring(1, genres.length() - 1).split(", "))
                .map(s -> s.split(";"))
                .filter(s -> s.length == 2)
                .map(s -> new Genre(Integer.parseInt(s[0]), s[1]))
                .collect(Collectors.toList());
    }

    private static List<Director> makeDirectorList(ResultSet rs) throws SQLException {
        var directors = rs.getString("directors");

        return Arrays.stream(directors.substring(1, directors.length() - 1).split(", "))
                .map(s -> s.split(";"))
                .filter(s -> s.length == 2)
                .map(s -> Director.builder().id(Integer.parseInt(s[0])).name(s[1]).build())
                .collect(Collectors.toList());
    }

    public static MapSqlParameterSource[] toGenreMap(Film film) {
        return film.getGenres().stream()
                .map(genre ->
                        new MapSqlParameterSource()
                                .addValue("film_id", film.getId())
                                .addValue("genre_id", genre.getId()))
                .toArray(MapSqlParameterSource[]::new);
    }

    public static MapSqlParameterSource[] toDirectorMap(Film film) {
        return film.getDirectors().stream()
                .map(director -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("director_id", director.getId()))
                .toArray(MapSqlParameterSource[]::new);
    }
}
