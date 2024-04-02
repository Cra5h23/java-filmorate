package ru.yandex.practicum.filmorate.service.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
public interface DirectorService {
    /**
     * Метод получения списка всех режесёров
     * @return список всех режесёров
     */
    List<Director> findAll();

    /**
     * Метод получения режесёра по id
     * @param id
     * @return
     */
    Director findById(Integer id);
}
