package ru.yandex.practicum.filmorate.service.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventDao;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.dao.user.UserDao;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final UserDao userDao;

    @Override
    public List<Event> getUserFeed(int userId) {
        checkUser(userId);
        return eventDao.getUserFeed(userId);
    }

    private User checkUser(Integer userId) {
        return userDao.getUserById(userId)
                .orElseThrow(() -> new UserServiceException(
                        format("Попытка запросить ленту пользователя с несуществующим id: %d", userId)));
    }
}
