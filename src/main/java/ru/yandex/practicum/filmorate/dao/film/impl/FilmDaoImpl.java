package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.film.FilmUtil;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        var sql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "GROUP BY f.FILM_ID\n";

        return jdbcTemplate.query(sql, FilmUtil::makeFilm);
    }

    @Override
    public Film addFilm(Film film) {
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        var simpleJdbcInsertToGenres = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_genres");
        var simpleJdbcInsertToDirectors = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films_directors");

        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmUtil.toMap(film)).intValue());
        simpleJdbcInsertToGenres.executeBatch(FilmUtil.toGenreMap(film));
        simpleJdbcInsertToDirectors.executeBatch(FilmUtil.toDirectorMap(film));

        return getFilmById(film.getId()).get();
    }

    @Override
    public Film updateFilm(Film film) {
        var sql = "update films " +
                "set " +
                "name = :name, " +
                "description = :description, " +
                "release_date = :release_date, " +
                "duration= :duration, " +
                "rating_id = :rating_id " +
                "where film_id = :film_id;" +
                "delete from film_genres where film_id = :film_id;" +
                "delete from films_directors where film_id = :film_id;";
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film_genres");
        var simpleJdbcInsert1 = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films_directors");

        namedParameterJdbcTemplate.update(sql, FilmUtil.toMap(film));
        simpleJdbcInsert.executeBatch(FilmUtil.toGenreMap(film));
        simpleJdbcInsert1.executeBatch(FilmUtil.toDirectorMap(film));

        return getFilmById(film.getId()).get();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        var param = new MapSqlParameterSource().addValue("film_id", id);
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var sql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "WHERE f.FILM_ID = :film_id\n" +
                "GROUP BY f.FILM_ID\n";

        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, FilmUtil::makeFilm));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteFilm(int id) {
        var param = new MapSqlParameterSource().addValue("film_id", id);
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        var sql = "delete from films where film_id = :film_id";

        namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    public Collection<Film> getSortedFilms(FilmSort sort, Object... params) {
        var popularFilmDescSql = "SELECT f.FILM_ID AS film_id, f.NAME AS name, " +
                "f.DESCRIPTION AS description, f.RELEASE_DATE AS release_date, " +
                "f.DURATION AS duration, r.RATING_ID AS rating_id, " +
                "r.RATING_NAME AS rating_code, " +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) AS genres, " +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) AS directors, " +
                "COUNT(l.USER_ID) AS likes_count " +
                "FROM FILMS AS f " +
                "LEFT JOIN RATINGS AS r ON r.RATING_ID = f.RATING_ID " +
                "LEFT JOIN FILM_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
                "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID " +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID = f.FILM_ID " +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID " +
                "LEFT JOIN LIKES l ON l.FILM_ID = f.FILM_ID ";

        var filmsByDirectorSortYearSql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "WHERE d.DIRECTOR_ID = ?\n" +
                "GROUP BY d.DIRECTOR_ID, F.FILM_ID\n" +
                "ORDER BY f.RELEASE_DATE \n";

        var filmsByDirectorSortLikesSql = "SELECT\n" +
                "f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "LEFT JOIN LIKES l ON l.FILM_ID = f.FILM_ID\n" +
                "WHERE d.DIRECTOR_ID = ?\n" +
                "GROUP BY d.DIRECTOR_ID , F.FILM_ID\n" +
                "ORDER BY COUNT(DISTINCT l.USER_ID) DESC\n";

        var commonFilms = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f \n" +
                "LEFT JOIN ratings r on f.rating_id=r.rating_id \n" +
                "LEFT JOIN LIKES l ON f.FILM_ID = l.FILM_ID \n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "LEFT JOIN LIKES l1 ON f.FILM_ID = l1.FILM_ID \n" +
                "LEFT JOIN LIKES l2 ON f.FILM_ID = l2.FILM_ID \n" +
                "WHERE l1.USER_ID = ? AND L2.USER_ID = ?\n" +
                "GROUP BY f.FILM_ID \n" +
                "ORDER BY COUNT(l.USER_ID) DESC ";

        List<Object> queryParams = new ArrayList<>();

        if (sort.equals(FilmSort.POPULAR_FILMS_DESC)) {
            Integer count = (Integer) params[0];
            Integer genreId = (Integer) params[1];
            Integer year = (Integer) params[2];

            boolean hasCondition = false;
            if (genreId != null || year != null) {
                popularFilmDescSql += " WHERE f.FILM_ID IN (SELECT DISTINCT fg.FILM_ID FROM FILM_GENRES fg " +
                        "JOIN FILMS f ON fg.FILM_ID = f.FILM_ID ";

                if (genreId != null) {
                    popularFilmDescSql += " WHERE fg.GENRE_ID = ?";
                    queryParams.add(genreId);
                    hasCondition = true;
                }

                if (year != null) {
                    popularFilmDescSql += (hasCondition ? " AND " : " WHERE ") + "EXTRACT(YEAR FROM f.RELEASE_DATE) = ?";
                    queryParams.add(year);
                }

                popularFilmDescSql += ") ";
            }

            popularFilmDescSql += "GROUP BY f.FILM_ID, r.RATING_ID ORDER BY likes_count DESC LIMIT ?";
            queryParams.add(count);
        }

        switch (sort) {
            case POPULAR_FILMS_DESC:
                return jdbcTemplate.query(popularFilmDescSql, FilmUtil::makeFilm, queryParams.toArray());
            case FILMS_BY_DIRECTOR:
                if ("year".equals(params[1])) {
                    return jdbcTemplate.query(filmsByDirectorSortYearSql, FilmUtil::makeFilm, params[0]);
                } else if ("likes".equals(params[1])) {
                    return jdbcTemplate.query(filmsByDirectorSortLikesSql, FilmUtil::makeFilm, params[0]);
                }
                return List.of();
            case COMMON_FILMS_DESC:
                return jdbcTemplate.query(commonFilms, FilmUtil::makeFilm, params[0], params[1]);
            default:
                return List.of();
        }
    }

    @Override
    public Collection<Film> getRecommendationFilms(Integer userId) {
        Object[] params = new Object[]{userId, userId, userId};
        String sql = "SELECT f.*,\n" +
                "r.RATING_NAME,\n" +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,\n" +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID\n" +
                "LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID\n" +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID\n" +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID\n" +
                "WHERE f.FILM_ID IN (\n" +
                "SELECT DISTINCT film_id FROM likes AS fl_2\n" +
                "WHERE user_id IN (\n" +
                "       SELECT DISTINCT user_id FROM likes AS fl_1\n" +
                "       WHERE\n" +
                "       fl_1.user_id != ?\n" +
                "       AND fl_1.film_id IN (SELECT fl_0.film_id FROM likes AS fl_0 WHERE fl_0.user_id = ?)\n" +
                "    )\n" +
                "    AND film_id NOT IN (SELECT fl_0.film_id FROM likes AS fl_0 WHERE fl_0.user_id = ?)\n" +
                ") " +
                "GROUP BY f.FILM_ID\n";
        log.info("Выполняется запрос к БД: {} Параметры: {}", sql, params);

        return jdbcTemplate.query(sql, FilmUtil::makeFilm, params);
    }

    @Override
    public Collection<Film> findFilms(String query, String by) {
        String sql = "SELECT f.FILM_ID AS film_id, f.NAME AS name, " +
                "f.DESCRIPTION AS description, f.RELEASE_DATE AS release_date, " +
                "f.DURATION AS duration, r.RATING_ID AS rating_id, " +
                "r.RATING_NAME AS rating_code, " +
                "ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) AS genres, " +
                "ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) AS directors, " +
                "COUNT(l.USER_ID) AS likes_count " +
                "FROM FILMS AS f " +
                "LEFT JOIN RATINGS AS r ON r.RATING_ID = f.RATING_ID " +
                "LEFT JOIN FILM_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
                "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID " +
                "LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID = f.FILM_ID " +
                "LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID " +
                "LEFT JOIN LIKES l ON l.FILM_ID = f.FILM_ID ";

        List<String> conditions = new ArrayList<>();

        String[] searchParams = by.split(",");
        for (String param : searchParams) {
            if (param.equals("title")) {
                conditions.add("f.NAME ILIKE '%" + query + "%'");
            } else if (param.equals("director")) {
                conditions.add("d.DIRECTOR_NAME ILIKE '%" + query + "%'");
            }
        }

        if (!conditions.isEmpty()) {
            sql += "WHERE " + String.join(" OR ", conditions) + " ";
        }

        sql += "GROUP BY f.FILM_ID, r.RATING_ID ORDER BY likes_count DESC";

        return jdbcTemplate.query(sql, FilmUtil::makeFilm);
    }
}