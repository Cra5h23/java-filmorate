package ru.yandex.practicum.filmorate.service.rating.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exeption.RatingServiceException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {
    private final RatingDao ratingDao;

    @Override
    public Collection<Rating> getAllRatings() {
        log.info("Запрошен список всех рейтингов");
        return (Collection<Rating>) ratingDao.findAll();
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        log.info("Запрошен рейтинг с id {}", ratingId);
        return ratingDao.findById(ratingId).orElseThrow(() -> new RatingServiceException(
                String.format("Запрашиваемый рейтинг с id %d не существует", ratingId)));
    }
}
