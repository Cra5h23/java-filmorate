package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс FilmController
 *
 * @author Nikolay Radzivon
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Getter
    private final Map<Integer, Film> filmMap = new HashMap<>();

    private int generatorFilmId = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        film.setId(++generatorFilmId);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int filmId = film.getId();
        Film f = filmMap.get(filmId);
        if (f == null) {
            log.warn("Нет фильма с id:" + filmId);
            throw new ValidationException("Нет фильма с id:" + filmId);
        }
        f.setName(film.getName());
        f.setDescription(film.getDescription());
        f.setReleaseDate(film.getReleaseDate());
        f.setDuration(film.getDuration());
        log.info("Обновлён пользователь с id: " + filmId);
        return f;
    }
}