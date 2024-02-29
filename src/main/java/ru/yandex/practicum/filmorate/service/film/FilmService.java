package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    /**
     * Метод получения списка всех фильмов
     *
     * @return коллекцию всех фильмов
     */
    Collection<Film> getFilms();

    /**
     * Метод получения фильма по id
     *
     * @param filmId идентификационный номер фильма
     * @return фильм с данным id
     */
    Film getFilmById(Integer filmId);

    /**
     * Метод добавления нового фильма
     *
     * @param film фильм
     * @return созданный фильм с id
     */
    Film addFilm(Film film);

    /**
     * Метод обновления фильма
     *
     * @param film фильм
     * @return фильм с обновлёнными полями
     */
    Film updateFilm(Film film);

    /**
     * Метод удаления фильма по заданному id
     *
     * @param filmId индификационный номер фильма который нужно удалить
     * @return Сообщение об успешном удалении фильма
     */
    String deleteFilmById(Integer filmId);
}
