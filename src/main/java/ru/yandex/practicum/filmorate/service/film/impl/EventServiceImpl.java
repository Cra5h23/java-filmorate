package ru.yandex.practicum.filmorate.service.film.impl;

import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.film.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {
    private EventDao eventDao;

    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public List<Event> getUserFeed(int userId) {
        return eventDao.getUserFeed(userId);
    }
}
