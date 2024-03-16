package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    //private int generatorFilmId = 0;

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
        //film.setId(++generatorFilmId);
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

//        f.setName(film.getName());
//        f.setDescription(film.getDescription());
//        f.setReleaseDate(film.getReleaseDate());
//        f.setDuration(film.getDuration());
//        filmStorage.updateFilm(f);
//        log.info("Обновлён фильм с id: {}", film.getId());
//        return f;
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
        return format("Удалён фильм с id: %d", filmId);
    }

    private Film checkFilm(Integer filmId, String s) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmServiceException(
                        format("Попытка %s фильм с несуществующим id: %d", s, filmId)));
    }
}
