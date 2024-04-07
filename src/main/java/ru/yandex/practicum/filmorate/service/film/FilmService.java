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

    /**
     * Метод получения списка всех фильмов заданного режиссёра
     * @param directorId
     * @param sortBy
     * @return
     */
    Collection<Film> getFilmsByDirector(Integer directorId, String sortBy);

    /**
     * Метод поиска фильмов по названию/режиссеру
     *
     * @param query — текст для поиска
     * @param by — может принимать значения director (поиск по режиссёру), title (поиск по названию),
     * либо оба значения через запятую при поиске одновременно и по режиссеру и по названию.
     * @return Список фильмов, отсортированных по популярности.
     */

    Collection<Film> findFilms(String query, String by);
}
