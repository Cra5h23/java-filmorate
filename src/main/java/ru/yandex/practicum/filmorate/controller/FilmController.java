package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Класс FilmController
 *
 * @author Nikolay Radzivon
 */
@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmRepository filmRepository;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @PostMapping()
    public Film addNewFilm(@Valid @RequestBody Film film) {
        return filmRepository.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
      return filmRepository.updateFilm(film);
    }
}