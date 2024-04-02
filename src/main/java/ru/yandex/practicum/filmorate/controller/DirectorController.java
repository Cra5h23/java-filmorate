package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<Director>> getAllDirectors() {
        log.info("GET /directors");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(directorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable(required = false) Integer id) {
        log.info("GET /directors0/{}", id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(directorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Director> createDirector(@RequestBody Director director) {
        log.info("POST /directors");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(directorService.addNewDirector());
    }

    @PutMapping
    public ResponseEntity<Director> updateDirector(@RequestBody Director director) {
        log.info("PUT /directors");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(directorService.updateDirector(director));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDirectorById(@PathVariable Integer id) {
        log.info("DELETE /directors/{}", id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(directorService.deleteDirectorById(id));
    }
}
