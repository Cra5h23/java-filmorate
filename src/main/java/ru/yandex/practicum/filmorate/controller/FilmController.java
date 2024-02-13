package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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
    private final Map<Integer, Film> filmMap = new HashMap<>();//todo перенести в хранилище

    private int generatorFilmId = 0; //todo

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    //@GetMapping
    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();


//        return filmMap.values();//todo
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(required = false) Integer id) {
        return filmStorage.getFilmById(id);
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);

//        film.setId(++generatorFilmId);
//        filmMap.put(film.getId(), film);
//        log.info("Добавлен фильм {}", film);
//        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
        /*int filmId = film.getId();
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
        return f;*/
    }

    @DeleteMapping
    public void deleteFilm(@RequestParam int id) {
        filmStorage.deleteFilm(id);
    }
}