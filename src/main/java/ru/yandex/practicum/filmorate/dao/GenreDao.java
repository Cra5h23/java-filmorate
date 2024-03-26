package ru.yandex.practicum.filmorate.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreDao extends CrudRepository<Genre, Integer> {
}
