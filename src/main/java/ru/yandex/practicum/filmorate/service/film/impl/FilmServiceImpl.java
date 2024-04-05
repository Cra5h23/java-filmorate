package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exeption.FilmLikeServiceException;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    private final DirectorDao directorDao;

    /**
     * Метод получения списка всех фильмов
     *
     * @return коллекцию всех фильмов
     */
    @Override
    public Collection<Film> getFilms() {
        log.info("Запрошен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    /**
     * @param filmId
     * @return
     */
    @Override
    public Film getFilmById(Integer filmId) {
        log.info("Запрошен фильм с id {}", filmId);
        return checkFilm(filmId, "получить");
    }

    /**
     * @param film
     * @return
     */
    @Override
    public Film addFilm(Film film) {
        log.info("Добавлен фильм {}", film);
        return filmStorage.addFilm(film);
    }

    /**
     * @param film
     * @return
     */
    @Override
    public Film updateFilm(Film film) {
        var f = checkFilm(film.getId(), "обновить");
        log.info("Обновлён фильм с id: {}", film.getId());
        return filmStorage.updateFilm(film);
    }

    /**
     * @param filmId
     * @return
     */
    @Override
    public String deleteFilmById(Integer filmId) {
        checkFilm(filmId, "удалить");
        filmStorage.deleteFilm(filmId);
        log.info("Удалён фильм с id: {}", filmId);
        return format("Удалён фильм с id: %d", filmId);
    }

    /**
     * Метод получения списка всех фильмов заданного режиссёра
     *
     * @param directorId
     * @param sortBy
     * @return
     */
    @Override
    public Collection<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        log.info("Запрошен списко всех фильмов для режиссёра с id {} и сортировкой по {}", directorId, sortBy);
        Optional<Director> byId = directorDao.findById(directorId);
        byId.orElseThrow(() -> new FilmLikeServiceException(format("Режиссёра с id %d не существует", directorId)));

        switch (sortBy) {
            case "year":
                return filmStorage.getSortedFilms(FilmSort.FILMS_BY_DIRECTOR_SORT_YEAR.getSql(), directorId);
            case "likes":
                return filmStorage.getSortedFilms(FilmSort.FILMS_BY_DIRECTOR_SORT_LIKES.getSql(), directorId);
            default:
                throw new IllegalStateException("Unexpected value: " + sortBy);
        }
    }

    private Film checkFilm(Integer filmId, String s) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmServiceException(
                        format("Попытка %s фильм с несуществующим id: %d", s, filmId)));
    }
}
