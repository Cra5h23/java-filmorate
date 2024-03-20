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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
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
        var filmsSql = "select f.*, r.rating_name from films f left join ratings r on f.rating_id=r.rating_id";
        var genreSql = "select fg.film_id, g.* from film_genres fg left join genres g on fg.genre_id=g.genre_id";
        List<Film> films = new ArrayList<>();
        final List<Map<Integer, Genre>> genreList = new ArrayList<>();

        try {
            var query = jdbcTemplate.query(filmsSql, this::makeFilm);
            films.addAll(query);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }

        try {
            var query = jdbcTemplate.query(genreSql, this::makeGenreMap);
            genreList.addAll(query);
        } catch (EmptyResultDataAccessException e) {
            genreList.addAll(List.of());
        }

        films.forEach(f -> f.setGenres(
                genreList.stream().filter(m -> m.containsKey(f.getId()))
                        .map(o -> o.get(f.getId()))
                        .collect(Collectors.toList()))
        );

        return films;
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

        film.getGenres().forEach(genre -> jdbcTemplate.update(s, keyHolder.getKey(), genre.getId()));
        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        var sql = "update films set name=?, description=?, release_date=?, duration=?, rating_id=? where film_id=?;";
        var s = "delete from film_genres where film_id=?";
        var genreSql = "insert into film_genres(film_id, genre_id) values(?, ?)";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (!film.getGenres().isEmpty()) {
            jdbcTemplate.update(s, film.getId());
            film.getGenres().forEach(g -> jdbcTemplate.update(genreSql, film.getId(), g.getId()));
        }

        return getFilmById(film.getId()).get();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        var filmSql = "select f.*, r.rating_name from films f left join ratings r on f.rating_id=r.rating_id where f.film_id=?";
        var ratingSql = "select fg.film_id, g.* from film_genres fg left join genres g on fg.genre_id=g.genre_id where fg.film_id =?";
        List<Map<Integer, Genre>> query = new ArrayList<>();
        Film film;

        try {
            film = jdbcTemplate.queryForObject(filmSql, this::makeFilm, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        try {
            List<Map<Integer, Genre>> query1 = jdbcTemplate.query(ratingSql, this::makeGenreMap, id);
            query.addAll(query1);
        } catch (EmptyResultDataAccessException e) {
            query = List.of();
        }
        film.setGenres(query.stream().filter(m -> m.containsKey(id)).map(o -> o.get(id)).collect(Collectors.toList()));

        return Optional.of(film);
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
        var genreSql = "select fg.film_id,\n" +
                "g.* \n" +
                "from film_genres fg \n" +
                "left join genres g on fg.genre_id=g.genre_id \n" +
                "left join likes l on fg.film_id=l.film_id\n" +
                "GROUP BY fg.film_id, g.genre_id\n" +
                "ORDER BY count(l.user_id) DESC\n" +
                "limit?";
        List<Film> films = new ArrayList<>();
        final List<Map<Integer, Genre>> genreList = new ArrayList<>();

        try {
            var query = jdbcTemplate.query(sort.getSql(), this::makeFilm, count);
            films.addAll(query);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }

        try {
            var query = jdbcTemplate.query(genreSql, this::makeGenreMap, count);
            genreList.addAll(query);
        } catch (EmptyResultDataAccessException e) {
            genreList.addAll(List.of());
        }

        films.forEach(f -> f.setGenres(
                genreList.stream().filter(m -> m.containsKey(f.getId()))
                        .map(o -> o.get(f.getId()))
                        .collect(Collectors.toList()))
        );

        return films;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(rs.getInt("rating_id") != 0 && rs.getString("rating_name") != null ?
                        new Rating(rs.getInt("rating_id"), rs.getString("rating_name"))
                        : new Rating())
                .build();
    }

    private Map<Integer, Genre> makeGenreMap(ResultSet rs, int rowNum) throws SQLException {
        return Map.of(rs.getInt("film_id"),
                new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }
}