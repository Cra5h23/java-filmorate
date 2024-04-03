package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDaoImpl implements DirectorDao {

    /**
     * Метод получения списка всех режесёров из базы данных
     *
     * @return список всех режесёров
     */
    @Override
    public List<Director> findAll() {
        return null;
    }
}
