package ru.yandex.practicum.filmorate.service.director.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorDao;
import ru.yandex.practicum.filmorate.exeption.DirectorServiceException;
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
     * Метод получения списка всех режиссёров
     *
     * @return список всех режиссёров
     */
    @Override
    public List<Director> getAllDirectors() {
        log.info("Запрошен список всех режиссёров");
        return directorDao.findAll();
    }

    /**
     * Метод получения режиссёра по id
     *
     * @param id
     * @return
     */
    @Override
    public Director findById(Integer id) {
        log.info("Запрошен режиссёр с id {}", id);
        return checkDirector(id, String.format("Попытка получить режиссёра с несуществующим id: %d", id));
    }

    /**
     * Метод создания нового режиссёра
     *
     * @return
     */
    @Override
    public Director addNewDirector(Director director) {
        var d = directorDao.save(director);
        log.info("Добавлен режиссёр {}", d);
        return d;
    }

    /**
     * Метод изменения режиссёра
     *
     * @param director
     * @return
     */
    @Override
    public Director updateDirector(Director director) {
        var id = director.getId();
        checkDirector(id, String.format("Попытка обновить режиссёра с несуществующим id %d",id));
        var d = directorDao.update(director);

        log.info("Обновлён режиссёр {}", d);
        return d;
    }

    /**
     * Метод удаления режиссёра по id
     *
     * @param id
     */
    @Override
    public void deleteDirectorById(Integer id) {
        checkDirector(id, String.format("Попытка удалить режесёра с несуществующим id %d", id));
        directorDao.delete(id);
    }

    private Director checkDirector(Integer id, String message) {
        return directorDao.findById(id).orElseThrow(() -> new DirectorServiceException(message));
    }
}
