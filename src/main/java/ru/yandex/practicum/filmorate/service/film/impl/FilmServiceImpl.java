package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    private final DirectorDao directorDao;

    private final LikeDao likeDao;

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
        log.info("Запрошен список всех фильмов для режиссёра с id {} и сортировкой по {}", directorId, sortBy);
        Optional<Director> byId = directorDao.findById(directorId);
        byId.orElseThrow(() -> new FilmServiceException(
                format("Нельзя получить список фильмов для не существующего режиссёра с id %d", directorId)));

        return filmStorage.getSortedFilms(FilmSort.FILMS_BY_DIRECTOR,directorId, sortBy);
    }

    @Override
    public Collection<Film> findFilms(String query, String by) {
        log.info("Выполнен поиск фильма по запросу {} и параметру {}", query, by);
        return filmStorage.findFilms(query,by);
    }

    private Film checkFilm(Integer filmId, String s) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmServiceException(
                        format("Попытка %s фильм с несуществующим id: %d", s, filmId)));
    }

    /**
     * Возвращает список фильмов рекомендованных пользователю.
     */
    @Override
    public Collection<Film> getRecommendationsByUserId(Integer userId) {
        List<Like> likes = likeDao.getAllFilmLikes();
        Set<Integer> currentUserLikes = new HashSet<>();
        Map<Integer, Set<Integer>> otherUsersLikes = new HashMap<>();
        Set<Integer> recommendationFilmIds = new HashSet<>();

        if (likes == null || likes.size() == 0) {
            return new ArrayList<>();
        }

        for (Like like : likes) {
            Integer likeUserId = like.getUserId();
            Integer likeFilmId = like.getFilmId();

            if (Objects.equals(likeUserId, userId)) {
                currentUserLikes.add(likeFilmId);
            } else {
                if (otherUsersLikes.containsKey(likeUserId)) {
                    otherUsersLikes.get(likeUserId).add(likeFilmId);
                } else {
                    Set<Integer> filmIds = new HashSet<>();
                    filmIds.add(likeFilmId);
                    otherUsersLikes.put(likeUserId, filmIds);
                }
            }
        }

        if (otherUsersLikes.size() == 0) {
            return new ArrayList<>();
        }

        for (Map.Entry<Integer, Set<Integer>> entry : otherUsersLikes.entrySet()) {
            Set<Integer> idsForCheck = new HashSet<>(entry.getValue());
            Set<Integer> idsTmp = new HashSet<>(entry.getValue());
            idsTmp.removeAll(currentUserLikes);

            if (idsTmp.equals(idsForCheck)) {
                continue;
            }

            recommendationFilmIds.addAll(idsTmp);
        }

        if (recommendationFilmIds.size() == 0) {
            return new ArrayList<>();
        }

        return filmStorage.getFilmsByIds(recommendationFilmIds);
    }
}
