package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        var sql = "SELECT " +
                "f.*,\n" +
                "STRING_AGG(fg.GENRE_ID, ', ') genres,\n" +
                "STRING_AGG(l.USER_ID, ', ') likes  \n" +
                "FROM FILMS f\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID \n" +
                "LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID \n" +
                "GROUP BY f.FILM_ID";
        try {
            return jdbcTemplate.query(sql, (this::makeFilm));
        }catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public Film addFilm(Film film) {
        var sql = "insert into films(name, description, release_date, duration, rating_id) values(?, ?, ?, ?, ?)";
        var s = "insert into film_genres(film_id, genre_id) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());

            return ps;
        }, keyHolder);
        film.setId((int) Objects.requireNonNull(keyHolder.getKey()));
        film.setLikes(Set.of());
        film.getGenres().forEach(genre -> jdbcTemplate.update(s, keyHolder.getKey(), genre.getId()));
        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        var sql = "update films set name=?, description=?, release_date=?, duration=? where film_id=?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        var sql = "select " +
                "f.*, \n" +
                "string_agg(fg.genre_id, ', ') genres, \n" +
                "string_agg(l.user_id, ', ') likes \n" +
                "from films f \n" +
                "left join film_genres fg on f.film_id = fg.film_id \n" +
                "left join likes l on f.film_id = l.film_id \n" +
                "where f.film_id = ? \n" +
                "group by f.film_id";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::makeFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteFilm(int id) {
        var sql = "delete from film_genres where film_id = ?;" +
                "delete from likes where film_id = ?;" +
                "delete from films where film_id = ?;";
        jdbcTemplate.update(sql, id, id, id);
    }

    @Override
    public Collection<Film> getSortedFilms(FilmSort sort, Integer count) {

        try {
            return jdbcTemplate.query(sort.getSql(), this::makeFilm, count);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("rating_id")))
                .genres(rs.getString("genres") != null ?
                        Arrays.stream(rs.getString("genres").split(", "))
                                .map(Long::parseLong)
                                .map(Genres::new)
                                .collect(Collectors.toList()) : List.of())
                .likes(rs.getString("likes") != null ?
                        Arrays.stream(rs.getString("likes").split(", "))
                                .map(Integer::parseInt).collect(Collectors.toSet()) : Set.of())
                .build();
    }
}