package ru.yandex.practicum.filmorate.util.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nikolay Radzivon
 */
public class FilmUtil {
    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
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
                .director(makeDirectorList(rs))
                .build();
    }

    public static Map<String, Object> toMap(Film film) {
        return Map.of("film_id", film.getId(),
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "rating_id", film.getMpa().getId());
    }

    private List<Genre> makeGenreList(ResultSet rs) throws SQLException {
        return Arrays.stream(rs.getString("genres").split(","))
                .map(s -> s.split(";"))
                .filter(s -> s.length == 2)
                .map(s -> new Genre(Integer.parseInt(s[0]), s[1]))
                .collect(Collectors.toList());
    }

    private List<Director> makeDirectorList(ResultSet rs) throws SQLException {
        return Arrays.stream(rs.getString("directors").split(","))
                .map(s-> s.split(";"))
                .filter(s->s.length==2)
                .map(s-> Director.builder().id(Integer.parseInt(s[0])).name(s[1]).build())
                .collect(Collectors.toList());
    }
}
