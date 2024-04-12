package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmDao {
    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(int id);

    void deleteFilm(int id);

    Collection<Film> getSortedFilms(FilmSort sort, Object... param);

    Collection<Film> findFilms(String query, String by);

    Collection<Film> getRecommendationFilms(Integer userId);
}
