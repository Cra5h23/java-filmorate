package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmStorageException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    @Getter
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int generatorFilmId = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return Collections.unmodifiableCollection(filmMap.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++generatorFilmId);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        var filmId = film.getId();
        var f = checkFilm(filmId);
        f.setName(film.getName());
        f.setDescription(film.getDescription());
        f.setReleaseDate(film.getReleaseDate());
        f.setDuration(film.getDuration());
        log.info("Обновлён фильм с id: {}", filmId);
        return f;
    }

    @Override
    public Film getFilmById(int id) {
        var f = checkFilm(id);
        log.info("Получен фильм с id: {}", id);
        return f;
    }

    @Override
    public String deleteFilm(int id) {
        checkFilm(id);
        log.info("Удалён фильм с id: {}", id);
        filmMap.remove(id);
        return String.format("Удалён фильм с id: %d", id);
    }

    private Film checkFilm(Integer id) {
        if (!filmMap.containsKey(id)) {
            log.warn("Фильм с id: {} не существует", id);
            throw new FilmStorageException(String.format("Фильм с id: %d не существует", id));
        }
        return filmMap.get(id);
    }
}
