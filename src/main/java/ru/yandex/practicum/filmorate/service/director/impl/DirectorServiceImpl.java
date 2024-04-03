package ru.yandex.practicum.filmorate.service.director.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.List;

/**
 * @author Nikolay Radzivon
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorDao directorDao;

    /**
     * Метод получения списка всех режесёров
     *
     * @return список всех режесёров
     */
    @Override
    public List<Director> getAllDirectors() {
        log.info("Запрошен список всех режесёров");
        return directorDao.findAll();
    }

    /**
     * Метод получения режесёра по id
     *
     * @param id
     * @return
     */
    @Override
    public Director findById(Integer id) {
        return null;
    }

    /**
     * Метод создания нового режесёра
     *
     * @return
     */
    @Override
    public Director addNewDirector() {
        return null;
    }

    /**
     * Метод изменения режесёра
     *
     * @param director
     * @return
     */
    @Override
    public Director updateDirector(Director director) {
        return null;
    }

    /**
     * Метод удаления режесёра по id
     *
     * @param id
     * @return
     */
    @Override
    public Director deleteDirectorById(Integer id) {
        return null;
    }
}
