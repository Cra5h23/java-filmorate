package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
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
        log.info("Запрошены все фильмы");
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
        int filmId = film.getId();
        Film f = filmMap.get(filmId);
        if (f == null) {
            log.warn("Нет фильма с id:" + filmId);
            throw new ValidationException(String.format("Нет фильма с id:%d", filmId));
        }
        f.setName(film.getName());
        f.setDescription(film.getDescription());
        f.setReleaseDate(film.getReleaseDate());
        f.setDuration(film.getDuration());
        log.info("Обновлён фильм с id: {}", filmId);
        return f;
    }

    @Override
    public Film getFilmById(int id) {
        var film = filmMap.get(id);
        if (film == null) {
            throw new ValidationException(String.format("Нет фильма с id:%d",id));
        }
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        if (!filmMap.containsKey(id)) {
            throw new ValidationException(String.format("Нет фильма с id:%s",id));
        }
        filmMap.remove(id);
    }
}
