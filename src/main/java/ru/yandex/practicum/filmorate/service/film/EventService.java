package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;


public interface EventService {
    List<Event> getUserFeed(int userId);
}
