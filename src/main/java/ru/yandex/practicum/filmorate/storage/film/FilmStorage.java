package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    void addFilm(Film film);

    void updateFilm(Film film);

    Optional<Film> getFilmById(int id);

    void deleteFilm(int id);

    Collection<Film> getSortedFilms(Comparator<Film> comparator, Integer count);
}
