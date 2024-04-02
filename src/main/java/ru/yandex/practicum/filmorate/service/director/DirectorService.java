package ru.yandex.practicum.filmorate.service.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
public interface DirectorService {

    List<Director> findAll();
}
