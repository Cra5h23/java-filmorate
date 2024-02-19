package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

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
@AllArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmStorage.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable(required = false) Integer id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmStorage.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addNewFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmStorage.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmStorage.updateFilm(film));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFilm(@RequestParam int id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmStorage.deleteFilm(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> userLikesFilm(@PathVariable(name = "id") Integer filmId, @PathVariable Integer userId) { //todo (id-> filmId)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.addLikeFilm(filmId, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> userRemoveLikeFromFilm(@PathVariable(name = "id") Integer filmId, @PathVariable Integer userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.deleteLikeFilm(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getListOfMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getMostPopularFilm(count));
    }
}