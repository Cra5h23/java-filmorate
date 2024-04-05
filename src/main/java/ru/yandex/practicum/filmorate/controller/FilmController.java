package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;
import ru.yandex.practicum.filmorate.service.film.FilmService;

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
    private final FilmLikeService filmLikeService;

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("GET /getAllFilms");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable(required = false) Integer id) {
        log.info("GET /getFilmById {}", id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addNewFilm(@Valid @RequestBody Film film) {
        log.info("POST /addNewFilm {}", film);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT /updateFilm {}", film);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable int id) {
        log.info("DELETE /deleteFilm {}", id);
        return ResponseEntity.ok(filmService.deleteFilmById(id));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<?> userLikesFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT /userLikesFilm  film {} user {}", filmId, userId);
        filmLikeService.addLikeFilm(filmId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<?> userRemoveLikeFromFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("DELETE /userRemoveLikeFromFilm film {} user {}", filmId, userId);
        return ResponseEntity.ok(filmLikeService.deleteLikeFilm(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getListOfMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET /getListOfMostPopularFilms");
        return ResponseEntity.ok(filmLikeService.getMostPopularFilm(count));
    }

    @GetMapping("/director/{directorId}")
    public ResponseEntity<Collection<Film>> getFilmsByDirector(@PathVariable Integer directorId, @RequestParam String sortBy) {
        log.info("GET /director/{}/{}", directorId, sortBy);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilmsByDirector(directorId, sortBy));
    }
}