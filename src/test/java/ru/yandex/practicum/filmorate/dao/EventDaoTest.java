package ru.yandex.practicum.filmorate.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.EventDaoImpl;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
class EventDaoTest {
    private EventDao eventDao;
    private Clock clock;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        eventDao = new EventDaoImpl(jdbcTemplate, clock);
        var userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.addUser(User.builder()
                .id(1)
                .login("TestLogin")
                .name("TestName")
                .email("testEmail@.com")
                .birthday(LocalDate.now().minusYears(18))
                .build());
    }

    @Test
    @DisplayName("Метод getUserFeed возвращает ленту событий")
    void getUserFeed() {
        eventDao.createAddFilmLikeEvent(1, 1);
        eventDao.createDeleteFilmLikeEvent(2, 1);
        eventDao.createAddReviewEvent(1, 1);
        eventDao.createUpdateReviewEvent(2, 1);
        eventDao.createDeleteReviewEvent(3, 1);
        eventDao.createAddUserFriendEvent(1, 4);
        eventDao.createDeleteUserFriendEvent(1, 5);
        List<Event> userFeed = eventDao.getUserFeed(1);

        Assertions.assertThat(userFeed)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        Event.builder()
                                .eventId(1)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.LIKE)
                                .operation(OperationType.ADD)
                                .entityId(1),
                        Event.builder()
                                .eventId(2)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.LIKE)
                                .operation(OperationType.REMOVE)
                                .entityId(2),
                        Event.builder()
                                .eventId(3)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.REVIEW)
                                .operation(OperationType.ADD)
                                .entityId(1),
                        Event.builder()
                                .eventId(4)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.REVIEW)
                                .operation(OperationType.UPDATE)
                                .entityId(2),
                        Event.builder()
                                .eventId(5)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.REVIEW)
                                .operation(OperationType.REMOVE)
                                .entityId(3),
                        Event.builder()
                                .eventId(6)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.FRIEND)
                                .operation(OperationType.ADD)
                                .entityId(4),
                        Event.builder()
                                .eventId(7)
                                .timestamp(Instant.now(clock).toEpochMilli())
                                .userId(1)
                                .eventType(EventType.FRIEND)
                                .operation(OperationType.REMOVE)
                                .entityId(5)));
    }
}