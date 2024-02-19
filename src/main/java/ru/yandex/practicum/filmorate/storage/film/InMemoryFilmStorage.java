package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmStorageException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int generatorFilmId = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return Collections.unmodifiableCollection(filmMap.values()); //todo должна ли быть неизменяемой?
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
    public void deleteFilm(int id) {
        checkFilm(id);
        log.info("Удалён фильм с id: {}", id);
        filmMap.remove(id);
    }

    private Film checkFilm(Integer id) {
        if (!filmMap.containsKey(id)) {
            log.warn("Фильм с id: {} не существует", id);
            throw new FilmStorageException(String.format("Фильм с id: %d не существует", id));
        }
        return filmMap.get(id);
    }
}
