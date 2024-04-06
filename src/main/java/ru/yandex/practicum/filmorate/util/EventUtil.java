package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EventUtil {

    public static Event makeEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("event_id"))
                .timeStamp(rs.getTimestamp("time_stamp").getTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }

    public static Map<String, Object> toMap(User user) {
        return Map.of("user_id", user.getId(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday());
    }
}