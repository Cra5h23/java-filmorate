package ru.yandex.practicum.filmorate.service.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
public interface DirectorService {
    /**
     * Метод получения списка всех режиссёров
     *
     * @return список всех режиссёров
     */
    List<Director> getAllDirectors();

    /**
     * Метод получения режиссёра по id
     *
     * @param id
     * @return
     */
    Director findById(Integer id);

    /**
     * Метод создания нового режиссёра
     *
     * @return
     */
    Director addNewDirector(Director director);

    /**
     * Метод изменения режиссёра
     *
     * @param director
     * @return
     */
    Director updateDirector(Director director);

    /**
     * Метод удаления режиссёра по id
     *
     * @param id
     */
    void deleteDirectorById(Integer id);
}
