package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikolay Radzivon
 */
public interface DirectorDao {
    /**
     * Метод получения списка всех режесёров из базы данных
     *
     * @return список всех режесёров
     */
    List<Director> findAll();

    Optional<Director> findById(Integer id);

    Director save(Director director);

    Director update(Director director);

    void delete(Integer id);

}
