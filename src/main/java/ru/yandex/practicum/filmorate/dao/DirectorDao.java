package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

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
}
