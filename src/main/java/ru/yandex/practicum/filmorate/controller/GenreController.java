package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.getAllGenre());
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<?> getGenreById(@PathVariable(required = false) Integer genreId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreService.getGenreById(genreId));
    }
}
