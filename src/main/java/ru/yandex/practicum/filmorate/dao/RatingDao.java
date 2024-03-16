package ru.yandex.practicum.filmorate.dao;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.filmorate.model.Rating;

public interface RatingDao extends CrudRepository<Rating, Integer> {
}
