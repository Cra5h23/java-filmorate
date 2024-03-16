package ru.yandex.practicum.filmorate.service.genre.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exeption.GenreServiceException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Collection<Genre> getAllGenre() {
        return (Collection<Genre>) genreDao.findAll();
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        return genreDao.findById(genreId).
                orElseThrow(() -> new GenreServiceException(
                        String.format("Запрашиваемый жанр с id %d не существует", genreId)));
    }
}
