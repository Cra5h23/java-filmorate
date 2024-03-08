package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public void addFilm(Film film) {

    }

    @Override
    public void updateFilm(Film film) {

    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.empty();
    }

    @Override
    public void deleteFilm(int id) {

    }

    @Override
    public Collection<Film> getSortedFilms(Comparator<Film> comparator, Integer count) {
        return null;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getInt("film_id"),
              rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"));
    }
}
