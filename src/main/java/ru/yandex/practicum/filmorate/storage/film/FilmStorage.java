package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(int id);

    void deleteFilm(int id);

    Collection<Film> getSortedFilms(FilmSort sort, Object... param);

    Collection<Film> getFilmsByIds(Set<Integer> filmIds);
}
