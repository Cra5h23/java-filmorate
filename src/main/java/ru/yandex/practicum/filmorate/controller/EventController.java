package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.film.EventService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/users/{userId}/feed")
    public ResponseEntity<?> getUserFeed(@PathVariable Integer userId) {
        log.info("GET getUserById {}", userId);
        return ResponseEntity
                .ok()
                .body(eventService.getUserFeed(userId));
    }
}

