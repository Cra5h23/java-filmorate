package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.util.event.EventUtil;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class EventDaoImpl implements EventDao {
    private final JdbcTemplate jdbcTemplate;
    private final Clock clock;

    @Override
    public List<Event> getUserFeed(int userId) {
        String sql = "SELECT * FROM EVENTS WHERE user_id = ?" +
                " ORDER BY time_stamp";
        return jdbcTemplate.query(sql, EventUtil::makeEvent, userId);
    }

    @Override
    public void createAddFilmLikeEvent(Integer filmId, Integer userId) {
        saveEvent(userId, EventType.LIKE, OperationType.ADD, filmId);
    }

    @Override
    public void createDeleteFilmLikeEvent(Integer filmId, Integer userId) {
        saveEvent(userId, EventType.LIKE, OperationType.REMOVE, filmId);
    }

    @Override
    public void createAddReviewEvent(Integer filmId, Integer userId) {
        saveEvent(userId, EventType.REVIEW, OperationType.ADD, filmId);
    }

    @Override
    public void createUpdateReviewEvent(Integer filmId, Integer userId) {
        saveEvent(userId, EventType.REVIEW, OperationType.UPDATE, filmId);
    }

    @Override
    public void createDeleteReviewEvent(Integer filmId, Integer userId) {
        saveEvent(userId, EventType.REVIEW, OperationType.REMOVE, filmId);
    }

    @Override
    public void createAddUserFriendEvent(Integer userId, Integer friendId) {
        saveEvent(userId, EventType.FRIEND, OperationType.ADD, friendId);
    }

    @Override
    public void createDeleteUserFriendEvent(Integer userId, Integer deletingFriendId) {
        saveEvent(userId, EventType.FRIEND, OperationType.REMOVE, deletingFriendId);
    }

    private void saveEvent(Integer userId, EventType eventType, OperationType operation, Integer entityId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("EVENTS")
                .usingGeneratedKeyColumns("EVENT_ID");
        insert.execute(Map.of(
                "time_stamp", Timestamp.from(Instant.now(clock)),
                "user_id", userId,
                "event_type", eventType.name(),
                "operation", operation.name(),
                "entity_id", entityId));
    }
}
