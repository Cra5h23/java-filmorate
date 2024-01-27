package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    private final int generatorId = 0;

    @GetMapping
    public Collection getAllFilms() {
        return Collections.unmodifiableCollection(films.values());
    }

    @PostMapping("/film")
    public Film addFilm(@RequestBody Film film) {
        return film;
    }


    private Film filmValidator (Film film) {
        if (film.getName().isBlank() || film.getName() == null) {

        }
    }
}
