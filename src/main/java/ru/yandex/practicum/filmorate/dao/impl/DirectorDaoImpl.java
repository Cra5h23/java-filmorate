package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.util.Director.DirectorUtil;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Метод получения списка всех режиссёров из базы данных
     *
     * @return список всех режиссёров
     */
    @Override
    public List<Director> findAll() {
        log.info("Получен запрос в базу данных на получение списка всех режиссёров");
        var sql = "select d.* from directors d";
        return jdbcTemplate.query(sql, DirectorUtil::makeDirector);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Director> findById(Integer id) {
        var sql = "select d.* from directors d where d.director_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, DirectorUtil::makeDirector, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * @param director
     * @return
     */
    @Override
    public Director save(Director director) {
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");

        director.setId(simpleJdbcInsert.executeAndReturnKey(DirectorUtil.toMap(director)).intValue());
        log.info("Создан запрос в базу данных на добавление режиссёра {}", director);
        return director;
    }

    /**
     * @param director
     * @return
     */
    @Override
    public Director update(Director director) {
        var sql = "update directors set director_name = ? where director_id = ?";

        jdbcTemplate.update(sql, director.getName(), director.getId());
        return director;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        var sql = "delete films_directors where director_id = ?;" +
                "delete directors where director_id = ?;";

        jdbcTemplate.update(sql, id, id);
    }
}
