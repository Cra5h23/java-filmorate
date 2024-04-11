package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.dao.director.DirectorDao;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.*;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
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
        return filmDao.getAllFilms();
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
        return filmDao.addFilm(film);
    }

    /**
     * @param film
     * @return
     */
    @Override
    public Film updateFilm(Film film) {
        var f = checkFilm(film.getId(), "обновить");
        log.info("Обновлён фильм с id: {}", film.getId());
        return filmDao.updateFilm(film);
    }

    /**
     * @param filmId
     * @return
     */
    @Override
    public String deleteFilmById(Integer filmId) {
        checkFilm(filmId, "удалить");
        filmDao.deleteFilm(filmId);
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

        return filmDao.getSortedFilms(FilmSort.FILMS_BY_DIRECTOR, directorId, sortBy);
    }

    @Override
    public Collection<Film> findFilms(String query, String by) {
        log.info("Выполнен поиск фильма по запросу {} и параметру {}", query, by);
        return filmDao.findFilms(query, by);
    }

    private Film checkFilm(Integer filmId, String s) {
        return filmDao.getFilmById(filmId)
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

        return filmDao.getFilmsByIds(recommendationFilmIds);
    }
}
