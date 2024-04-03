package ru.yandex.practicum.filmorate.util.Director;

import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Nikolqy Radzivon
 */
public class DirectorUtil {
    public static Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("director_name"))
                .build();
    }

    public static Map<String, Object> toMap(Director director) {
        return Map.of("director_id", director.getId(),
                "director_name", director.getName());
    }
}
