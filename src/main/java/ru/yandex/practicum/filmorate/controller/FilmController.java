package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
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
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    private final int generatorId = 0;

    @GetMapping
    public Collection getAllFilms() {
        return Collections.unmodifiableCollection(films.values());
    }

    @PostMapping("/film")
    public Film addFilm(@RequestBody Film film) {
        filmValidator(film);
        film.setId(++generatorId);
        films.put(film.getId(), film);
        return film;
    }


    private void filmValidator(Film film) {
        int validationDescriptionLength = 200;
        Instant validationReleaseDate = Instant.parse("1985-12-28T");

        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Имя фильма не должно быть пустым");
        }
        if (film.getDescription().length() > validationDescriptionLength) {
            throw new ValidationException("Длинна описания не должна привышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(validationReleaseDate)) {
            throw new ValidationException("Дата релиза не должна быть раньше " + validationReleaseDate);
        }

        if (film.getDuration() > 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
