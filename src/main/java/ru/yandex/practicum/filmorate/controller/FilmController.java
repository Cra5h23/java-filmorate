package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("filmLikeServiceDbImpl")
    private final FilmLikeService filmLikeService;

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable(required = false) Integer id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addNewFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable int id) {
        return ResponseEntity.ok(filmService.deleteFilmById(id));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<?> userLikesFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("PUT userLikesFilm  film {} user {}", filmId, userId);
        filmLikeService.addLikeFilm(filmId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<?> userRemoveLikeFromFilm(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return ResponseEntity.ok(filmLikeService.deleteLikeFilm(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getListOfMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET getListOfMostPopularFilms");
        return ResponseEntity.ok(filmLikeService.getMostPopularFilm(count));
    }
}