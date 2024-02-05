package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
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
        return Collections.unmodifiableCollection(filmMap.values());
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        film.setId(++generatorFilmId);
        filmMap.put(film.getId(), film);
        log.info("Добавлена задача {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int filmId = film.getId();
        try {
            return filmMap.keySet().stream()
                    .filter(id -> id.equals(filmId))
                    .map(filmMap::get)
                    .peek(f -> f.setName(film.getName()))
                    .peek(f -> f.setReleaseDate(film.getReleaseDate()))
                    .peek(f -> f.setDuration(film.getDuration()))
                    .peek(f -> f.setDescription(film.getDescription()))
                    .peek(f -> log.info("Обновлён фильм с id:{}", filmId))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Нет фильма с id:" + filmId));
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}