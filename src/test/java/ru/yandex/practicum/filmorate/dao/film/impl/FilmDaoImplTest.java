package ru.yandex.practicum.filmorate.dao.film.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.dao.like.impl.LikeDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.impl.UserDaoImpl;

import java.time.LocalDate;
import java.util.*;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
class FilmDaoImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Метод getAllFilms возвращает коллекцию всех фильмов из базы данных")
    void getAllFilmsTest() {
        Collection<Film> films = generatorFilmList(3);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        films.forEach(filmDbStorage::addFilm);
        Collection<Film> allFilms = filmDbStorage.getAllFilms();

        Assertions.assertThat(allFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @Test
    @DisplayName("Метод addFilm добавляет фильм в базу данных")
    void addFilmTest() {
        List<Film> films = generatorFilmList(1);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        Film film = films.stream().findFirst().map(filmDbStorage::addFilm).get();
        Film filmById = filmDbStorage.getFilmById(film.getId()).get();

        Assertions.assertThat(filmById)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("Метод updateFilm обновляет пользователя в базе данных")
    void updateFilmTest() {
        List<Film> films = generatorFilmList(2);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);

        Film film = filmDbStorage.addFilm(films.get(0));
        Film updateFilm = films.get(1);
        updateFilm.setId(film.getId());

        filmDbStorage.updateFilm(updateFilm);

        Film updatedFilm = filmDbStorage.getFilmById(updateFilm.getId()).get();

        Assertions.assertThat(updatedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updateFilm);
    }

    @Test
    void getFilmById() {
        List<Film> films = generatorFilmList(1);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        Film film = filmDbStorage.addFilm(films.get(0));

        int id = film.getId();

        Film f = filmDbStorage.getFilmById(id).get();

        Assertions.assertThat(f)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void deleteFilm() {
        List<Film> films = generatorFilmList(1);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        Film film = filmDbStorage.addFilm(films.get(0));
        int id = film.getId();

        filmDbStorage.deleteFilm(id);

        Optional<Film> filmById = filmDbStorage.getFilmById(id);

        Assertions.assertThat(filmById)
                .isNotPresent();
    }

    @Test
    void getSortedFilms() {
        List<Film> films = generatorFilmList(3);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        var userDbStorage = new UserDaoImpl(jdbcTemplate);
        LikeDaoImpl likeDao = new LikeDaoImpl(jdbcTemplate);

        List<User> users = List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build(),
                User.builder()
                        .id(3)
                        .name("testName3")
                        .login("testLogin3")
                        .email("testEmail3@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        );

        users.forEach(userDbStorage::addUser);

        Film film = filmDbStorage.addFilm(films.get(0));
        Film film1 = filmDbStorage.addFilm(films.get(1));
        Film film2 = filmDbStorage.addFilm(films.get(2));
        Collection<Film> filmCollection = new ArrayList<>(List.of(film, film2, film1));

        likeDao.saveLike(film.getId(), 1);
        likeDao.saveLike(film.getId(), 2);
        likeDao.saveLike(film.getId(), 3);
        likeDao.saveLike(film2.getId(), 2);

        Collection<Film> sortedFilms = filmDbStorage.getSortedFilms(FilmSort.POPULAR_FILMS_DESC, 10);

        Assertions.assertThat(sortedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmCollection);
    }

    @Test
    void getCommonFilms() {
        List<Film> films = generatorFilmList(5);
        var filmDbStorage = new FilmDaoImpl(jdbcTemplate);
        var userDbStorage = new UserDaoImpl(jdbcTemplate);
        LikeDaoImpl likeDao = new LikeDaoImpl(jdbcTemplate);

        List<User> users = List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build(),
                User.builder()
                        .id(3)
                        .name("testName3")
                        .login("testLogin3")
                        .email("testEmail3@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        );

        users.forEach(userDbStorage::addUser);

        Film film = filmDbStorage.addFilm(films.get(0));
        Film film1 = filmDbStorage.addFilm(films.get(1));
        Film film2 = filmDbStorage.addFilm(films.get(2));
        Film film3 = filmDbStorage.addFilm(films.get(3));
        Film film4 = filmDbStorage.addFilm(films.get(4));
        Collection<Film> filmCollection = new ArrayList<>(List.of(film4, film3));

        likeDao.saveLike(film.getId(), 1);
        likeDao.saveLike(film1.getId(), 2);
        likeDao.saveLike(film2.getId(), 3);
        likeDao.saveLike(film3.getId(), 1);
        likeDao.saveLike(film3.getId(), 2);
        likeDao.saveLike(film4.getId(), 1);
        likeDao.saveLike(film4.getId(), 2);
        likeDao.saveLike(film4.getId(), 3);

        Collection<Film> sortedFilms = filmDbStorage.getSortedFilms(FilmSort.COMMON_FILMS_DESC,1, 2);

        Assertions.assertThat(sortedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmCollection);
    }

    @Test
    void getRecommendation_returnVoid_BecauseNoIntersections() {
        var films = generatorFilmList(2);
        var filmDao = new FilmDaoImpl(jdbcTemplate);
        var likeDao = new LikeDaoImpl(jdbcTemplate);
        var userDao = new UserDaoImpl(jdbcTemplate);

        List<User> users = List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        );
        filmDao.addFilm(films.get(0));
        filmDao.addFilm(films.get(1));
        userDao.addUser(users.get(0));
        userDao.addUser(users.get(1));
        likeDao.saveLike(1, 1);
        likeDao.saveLike(2, 2);

        Collection<Film> gettedFilms = filmDao.getRecommendationFilms(1);

        Assertions.assertThat(gettedFilms)
                .isNotNull()
                .isEqualTo(List.of());
    }

    @Test
    void getRecommendation_returnVoid_BecauseNoIntersectionsLikesTheSame() {
        var films = generatorFilmList(1);
        var filmDao = new FilmDaoImpl(jdbcTemplate);
        var likeDao = new LikeDaoImpl(jdbcTemplate);
        var userDao = new UserDaoImpl(jdbcTemplate);

        List<User> users = List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        );
        filmDao.addFilm(films.get(0));
        userDao.addUser(users.get(0));
        userDao.addUser(users.get(1));
        likeDao.saveLike(1, 1);
        likeDao.saveLike(1, 2);

        Collection<Film> gettedFilms = filmDao.getRecommendationFilms(1);

        Assertions.assertThat(gettedFilms)
                .isNotNull()
                .isEqualTo(List.of());
    }

    @Test
    void getRecommendation() {
        var films = generatorFilmList(3);
        var filmDao = new FilmDaoImpl(jdbcTemplate);
        var likeDao = new LikeDaoImpl(jdbcTemplate);
        var userDao = new UserDaoImpl(jdbcTemplate);

        List<User> users = List.of(
                User.builder()
                        .id(1)
                        .name("testName1")
                        .login("testLogin1")
                        .email("testEmail1@test.com")
                        .birthday(LocalDate.parse("1989-03-05"))
                        .build(),
                User.builder()
                        .id(2)
                        .name("testName2")
                        .login("testLogin2")
                        .email("testEmail2@test.com")
                        .birthday(LocalDate.parse("1989-10-05"))
                        .build()
        );
        filmDao.addFilm(films.get(0));
        filmDao.addFilm(films.get(1));
        filmDao.addFilm(films.get(2));
        userDao.addUser(users.get(0));
        userDao.addUser(users.get(1));
        likeDao.saveLike(1, 1);
        likeDao.saveLike(2, 1);
        likeDao.saveLike(1, 2);
        likeDao.saveLike(3, 2);

        Collection<Film> gettedFilms = filmDao.getRecommendationFilms(1);

        Assertions.assertThat(gettedFilms)
                .isNotNull()
                .isEqualTo(List.of(films.get(2)));
    }

    private List<Film> generatorFilmList(Integer count) {
        List<Film> films = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            var f = Film.builder()
                    .id(i)
                    .name("testName" + i)
                    .description("testDescription" + i)
                    .mpa(new Rating(1, "G"))
                    .duration(10 + i)
                    .genres(Set.of())
                    .releaseDate(LocalDate.parse("1991-10-01").plusDays(i))
                    .build();
            films.add(f);
        }
        return films;
    }
}