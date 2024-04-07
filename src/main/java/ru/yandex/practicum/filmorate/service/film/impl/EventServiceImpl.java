package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.EventService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final UserStorage userStorage;

    @Override
    public List<Event> getUserFeed(int userId) {
        checkUser(userId);
        return eventDao.getUserFeed(userId);
    }

    public User checkUser(Integer userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserServiceException(
                        format("Попытка запросить пользователя с несуществующим id: %d", userId)));
    }
}
