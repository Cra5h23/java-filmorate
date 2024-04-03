package ru.yandex.practicum.filmorate.service.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
public interface DirectorService {
    /**
     * Метод получения списка всех режесёров
     *
     * @return список всех режесёров
     */
    List<Director> getAllDirectors();

    /**
     * Метод получения режесёра по id
     *
     * @param id
     * @return
     */
    Director findById(Integer id);

    /**
     * Метод создания нового режесёра
     *
     * @return
     */
    Director addNewDirector();

    /**
     * Метод изменения режесёра
     *
     * @param director
     * @return
     */
    Director updateDirector(Director director);

    /**
     * Метод удаления режесёра по id
     *
     * @param id
     * @return
     */
    Director deleteDirectorById(Integer id);
}
