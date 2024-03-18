package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Component("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int generatorFilmId = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return Collections.unmodifiableCollection(filmMap.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++generatorFilmId);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        Film f = filmMap.get(id);

        log.info("Обновлён фильм с id: {}", film.getId());
        f.setName(film.getName());
        f.setDescription(film.getDescription());
        f.setReleaseDate(film.getReleaseDate());
        f.setDuration(film.getDuration());
        f.setLikes(film.getLikes());
        return f;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        log.info("Запрошен фильм с id: {}", id);
        return Optional.ofNullable(filmMap.get(id));
    }

    @Override
    public void deleteFilm(int id) {
        log.info("Удалён фильм с id: {}", id);
        filmMap.remove(id);
    }

    /**
     * @param count
     * @return
     */
    @Override
    public Collection<Film> getSortedFilms(FilmSort sort, Integer count) {
        log.info("Запрошен сортированный список с {} элементами", count);
        return filmMap.values().stream().sorted(sort.getComparator()).limit(count).collect(Collectors.toList());
    }
}
