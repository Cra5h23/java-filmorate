package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.util.film.FilmUtil;

import java.util.Collection;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        var sql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "GROUP BY f.FILM_ID\n";

        return jdbcTemplate.query(sql, FilmUtil::makeFilm);
    }

    @Override
    public Film addFilm(Film film) {
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        var simpleJdbcInsertToGenres = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_genres");
        var simpleJdbcInsertToDirectors = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films_directors");

        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmUtil.toMap(film)).intValue());
        simpleJdbcInsertToGenres.executeBatch(FilmUtil.toGenreMap(film));
        simpleJdbcInsertToDirectors.executeBatch(FilmUtil.toDirectorMap(film));

        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        var sql = "update films set name=?, description=?, release_date=?, duration=?, rating_id=? where film_id=?;" +
                "delete from film_genres where film_id=?;" +
                "delete from films_directors where film_id=?;";
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_genres");
        var simpleJdbcInsert1 = new SimpleJdbcInsert(jdbcTemplate).withTableName("films_directors");

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId(), film.getId(), film.getId());
        simpleJdbcInsert.executeBatch(FilmUtil.toGenreMap(film));
        simpleJdbcInsert1.executeBatch(FilmUtil.toDirectorMap(film));

        return getFilmById(film.getId()).get();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        var sql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "WHERE f.FILM_ID = ?\n" +
                "GROUP BY f.FILM_ID\n" +
                "ORDER BY genres DESC";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, FilmUtil::makeFilm, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteFilm(int id) {
        var sql = "delete from films where film_id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public Collection<Film> getSortedFilms(String sql, Object... param) {
        return jdbcTemplate.query(sql, FilmUtil::makeFilm, param);
    }
}