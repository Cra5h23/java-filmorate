package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exeption.FilmLikeServiceException;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmService filmService;

    @MockBean
    @Qualifier("filmLikeServiceDbImpl")
    FilmLikeService filmLikeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /films возвращает коллекцию из двух фильмов")
    void getAllFilmsTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films");
        Mockito.when(filmService.getFilms()).thenReturn(generatorFilmList(2));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1," +
                        "\"name\":\"testName1\"," +
                        "\"description\":\"testDescription1\"," +
                        "\"releaseDate\":\"1989-05-02\"," +
                        "\"duration\":121," +
                        "\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":2," +
                        "\"name\":\"testName2\"," +
                        "\"description\":\"testDescription2\"," +
                        "\"releaseDate\":\"1989-05-02\"," +
                        "\"duration\":122," +
                        "\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}]")
        );
    }

    private List<Film> generatorFilmList(int count) {
        ArrayList<Film> films = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            var f = Film.builder()
                    .id(i)
                    .name("testName" + i)
                    .description("testDescription" + i)
                    .mpa(new Rating(1, "Комедия"))
                    .duration(120 + i)
                    .releaseDate(LocalDate.of(1989, 5, 1).plusDays(1))
                    .genres(Set.of(new Genre(1, "G")))
                    .build();
            films.add(f);
        }
        return films;
    }

    @Test
    @DisplayName("GET /films/1 возвращает фильм")
    void getUserById_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");

        Mockito.when(filmService.getFilmById(1)).thenReturn(generatorFilmList(1).get(0));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1," +
                        "\"name\":\"testName1\"," +
                        "\"description\":\"testDescription1\"," +
                        "\"releaseDate\":\"1989-05-02\"," +
                        "\"duration\":121," +
                        "\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}"));
    }

    @Test
    @DisplayName("GET /films/1 возвращает статус 404 и тело ошибки")
    void getUserById_ReturnsNotValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");

        Mockito.when(filmService.getFilmById(1))
                .thenThrow(new FilmServiceException("Попытка получить фильм с несуществующим id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с фильмами\":\"Попытка получить фильм с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /films создаёт новый фильм, возвращает статус 201")
    void addNewFilmTestValid() throws Exception {
        var f = Film.builder()
                .name("testName")
                .description("testDescription")
                .mpa(new Rating(1, "Комедия"))
                .duration(120)
                .releaseDate(LocalDate.of(1989, 5, 1).plusDays(1))
                .genres(Set.of(new Genre(1, "G")))
                .build();
        String s = objectMapper.writeValueAsString(f);
        f.setId(1);
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content(s);

        Mockito.when(filmService.addFilm(Mockito.any())).thenReturn(f);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1," +
                        "\"name\":\"testName\"," +
                        "\"description\":\"testDescription\"," +
                        "\"releaseDate\":\"1989-05-02\"," +
                        "\"duration\":120," +
                        "\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}")
        );
    }

    @Test
    @DisplayName("POST /films не создаёт фильм с пустым именем и возвращает код 404 и сообщение ошибке в тело ответа")
    void addNewFilmTestNotValidName() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":120})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Название фильма не должно быть пустым\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST /films не создаёт фильм с длинной описание больше 200 символов " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void addNewFilmTestNotValidDescriptionLength201() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"wYiSt0OoVdQCGDosdaadsadaaaaaaaaasaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaagHdl7j5AsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaagF94YaSBzy7te3L" +
                        "J29abX162KadadtDKftk3DV3qExTQ9jOPP1zO1LZGg6dMNKuMQuK3FYI00QkByZmmHQd64jylIhKVEcBj13Dpg" +
                        "FhJwBtCrcZslVOU\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":120})");
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":" +
                        "\"Описание фильма не должно быть меньше 1 и больше 200\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST /films не создаёт фильм с длинной описание меньше 1 символа " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void addNewFilmTestNotValidDescriptionLength0() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":120})");
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"" +
                        "Описание фильма не должно быть меньше 1 и больше 200\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST /films не создаёт фильм если дата релиза раньше 28-12-1895 " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void addNewFilmTestNotValidReleaseData() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"1894-10-10\"," +
                        "\"duration\":120})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Дата релиза не должна быть раньше 1895-12-28\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("POST films не создаёт фильм с отрицательной длительностью " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void addNewFilmTestNotValidDuration() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":-120})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"" +
                        "Продолжительность фильма должна быть положительной\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films обновляет фильм, и возвращает код ответа 200")
    void updateFilmTestValid() throws Exception {
        var f = Film.builder()
                .id(1)
                .name("testName")
                .description("testDescription")
                .mpa(new Rating(1, "Комедия"))
                .duration(120)
                .releaseDate(LocalDate.of(1989, 5, 1).plusDays(1))
                .genres(Set.of(new Genre(1, "G")))
                .build();

        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"testName\"," +
                        "\"description\":\"testDescription\"," +
                        "\"releaseDate\":\"1989-05-01\"," +
                        "\"duration\":120})");

        Mockito.when(filmService.updateFilm(Mockito.any())).thenReturn(f);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1," +
                        "\"name\":\"testName\"," +
                        "\"description\":\"testDescription\"," +
                        "\"releaseDate\":\"1989-05-02\"," +
                        "\"duration\":120," +
                        "\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}")
        );
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм если передан фильм с пустым именем " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void updateFilmTestNotValidName() throws Exception {
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"\"," +
                        "\"description\":\"testDescription\"," +
                        "\"releaseDate\":\"1980-10-10\"," +
                        "\"duration\":120})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Название фильма не должно быть пустым\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм если описание больше 200 символов " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void updateFilmTestNotValidDescriptionLength() throws Exception {
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"TestFilm1Update\"," +
                        "\"description\":\"SE3Qwr9lTHLmMflOygSaJ7iPcfmiwHUf4qRGW754wRYQxvl1B31" +
                        "DVxo5jqHpEEMEowYiSt0OoVdQCGDogHdl7j5AsgF94YaSBzy7te3LJ29abX162KtDKftk3" +
                        "DV3qExTQ9jOPP1zO1LZGg6dMNKuMQuK3FYI00QkByZmmHQd64jylIhKVEcBj13DpgFhJwBtCrcZslVOU\"," +
                        "\"releaseDate\":\"1980-10-10\"," +
                        "\"duration\":200})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"" +
                        "Описание фильма не должно быть меньше 1 и больше 200\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм если если дата релиза раньше 28-12-1895" +
            " и возвращает код 400 и сообщение ошибки в тело ответа")
    void updateFilmTestNotValid() throws Exception {
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"TestFilm1Update\"," +
                        "\"description\":\"TestDescription1update\"," +
                        "\"releaseDate\":\"1780-10-10\"," +
                        "\"duration\":200})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Дата релиза не должна быть раньше 1895-12-28\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм если передан фильм с отрицательной " +
            "длительностью и возвращает код 400 и сообщение ошибки в тело ответа")
    void updateFilmTestNotValidDuration() throws Exception {
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"TestFilm1Update\"," +
                        "\"description\":\"TestDescription1update\"," +
                        "\"releaseDate\":\"1980-10-10\"," +
                        "\"duration\":-200})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":" +
                        "\"Продолжительность фильма должна быть положительной\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /films/{id} удаляет фильм с заданным ид если он существует")
    void deleteFilmTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = delete("/films/1");

        Mockito.when(filmService.deleteFilmById(1)).thenReturn("Удалён фильм с id: 1");
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Удалён фильм с id: 1")
        );
    }

    @Test
    @DisplayName("DELETE /films/{id} не удаляет фильм с заданным ид если он не существует и возвращает код 404")
    void deleteFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = delete("/films/1");

        Mockito.when(filmService.deleteFilmById(1))
                .thenThrow(new FilmServiceException("Попытка удалить фильм с несуществующим id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с фильмами\":\"Попытка удалить фильм с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} устанавливает лайк фильму с заданым filmId " +
            "от пользователя с заданным userId и возвращает статус 200")
    void userLikesFilmTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = put("/films/1/like/1");

        Mockito.when(filmLikeService.addLikeFilm(1, 1)).thenReturn(null);
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} не устанавливает лайк фильму " +
            "с не существующим filmId от пользователя с заданным userId и выдаёт в ответ код 404 и сообщение ошибки")
    void userLikesFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = put("/films/1/like/1");
        Mockito.when(filmLikeService.addLikeFilm(1, 1))
                .thenThrow(new FilmLikeServiceException("Попытка добавить лайк фильму с несуществующим id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с лайками\":" +
                        "\"Попытка добавить лайк фильму с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} не устанавливает лайк фильму " +
            "с заданным filmId от пользователя с не существующим userId и выдаёт в ответ код 404 и сообщение ошибки")
    void userLikesFilmTest_NotExistsUser() throws Exception {
        var requestBuilder = put("/films/1/like/1");
        Mockito.when(filmLikeService.addLikeFilm(1, 1))
                .thenThrow(new FilmLikeServiceException("Попытка добавить лайк фильму от несуществующего пользователя c id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка работы с лайками\":" +
                        "\"Попытка добавить лайк фильму от несуществующего пользователя c id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} удаляет лайк у заданного фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTestValid() throws Exception {
        var requestBuilder = delete("/films/1/like/1");

        Mockito.when(filmLikeService.deleteLikeFilm(1, 1))
                .thenReturn("Пользователь с id: 1 удалил лайк у фильма с id: 1");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Пользователь с id: 1 удалил лайк у фильма с id: 1")
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} не удаляет лайк у несуществующего фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = delete("/films/1/like/1");

        Mockito.when(filmLikeService.deleteLikeFilm(1, 1))
                .thenThrow(new FilmLikeServiceException("Попытка удалить лайк у фильма с несуществующим id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().json("{\"Ошибка работы с лайками\":" +
                        "\"Попытка удалить лайк у фильма с несуществующим id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} не удаляет лайк у несуществующего фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTest_NotExistsUser() throws Exception {
        var requestBuilder = delete("/films/1/like/1");
        Mockito.when(filmLikeService.deleteLikeFilm(1, 1))
                .thenThrow(new FilmLikeServiceException(
                        "Попытка удалить лайк у фильма от несуществующего пользователя c id: 1"));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().json("{\"Ошибка работы с лайками\":" +
                        "\"Попытка удалить лайк у фильма от несуществующего пользователя c id: 1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 10 самых популярных фильмов по умолчанию")
    void getListOfMostPopularFilmsTest_ReturnsDefaultListMostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular");
        Mockito.when(filmLikeService.getMostPopularFilm(10)).thenReturn(List.of(
                Film.builder()
                        .id(1)
                        .name("TestFilm1")
                        .description("TestDescription1")
                        .releaseDate(LocalDate.parse("1900-01-02"))
                        .duration(2)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(11)
                        .name("TestFilm11")
                        .description("TestDescription11")
                        .releaseDate(LocalDate.parse("1900-01-12"))
                        .duration(12)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(4)
                        .name("TestFilm4")
                        .description("TestDescription4")
                        .releaseDate(LocalDate.parse("1900-01-05"))
                        .duration(5)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(5)
                        .name("TestFilm5")
                        .description("TestDescription5")
                        .releaseDate(LocalDate.parse("1900-01-06"))
                        .duration(6)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(7)
                        .name("TestFilm7")
                        .description("TestDescription7")
                        .releaseDate(LocalDate.parse("1900-01-08"))
                        .duration(8)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(9)
                        .name("TestFilm9")
                        .description("TestDescription9")
                        .releaseDate(LocalDate.parse("1900-01-10"))
                        .duration(10)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(10)
                        .name("TestFilm10")
                        .description("TestDescription10")
                        .releaseDate(LocalDate.parse("1900-01-11"))
                        .duration(11)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(6)
                        .name("TestFilm6")
                        .description("TestDescription6")
                        .releaseDate(LocalDate.parse("1900-01-07"))
                        .duration(7)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(8)
                        .name("TestFilm8")
                        .description("TestDescription8")
                        .releaseDate(LocalDate.parse("1900-01-09"))
                        .duration(9)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(3)
                        .name("TestFilm3")
                        .description("TestDescription3")
                        .releaseDate(LocalDate.parse("1900-01-04"))
                        .duration(4)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build()
        ));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"1900-01-02\",\"duration\":2,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":11,\"name\":\"TestFilm11\",\"description\":\"TestDescription11\"," +
                        "\"releaseDate\":\"1900-01-12\",\"duration\":12,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":4,\"name\":\"TestFilm4\",\"description\":\"TestDescription4\"," +
                        "\"releaseDate\":\"1900-01-05\",\"duration\":5,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":5,\"name\":\"TestFilm5\",\"description\":\"TestDescription5\"," +
                        "\"releaseDate\":\"1900-01-06\",\"duration\":6,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":7,\"name\":\"TestFilm7\",\"description\":\"TestDescription7\"," +
                        "\"releaseDate\":\"1900-01-08\",\"duration\":8,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":9,\"name\":\"TestFilm9\",\"description\":\"TestDescription9\"," +
                        "\"releaseDate\":\"1900-01-10\",\"duration\":10,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":10,\"name\":\"TestFilm10\",\"description\":\"TestDescription10\"," +
                        "\"releaseDate\":\"1900-01-11\",\"duration\":11,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":6,\"name\":\"TestFilm6\",\"description\":\"TestDescription6\"," +
                        "\"releaseDate\":\"1900-01-07\",\"duration\":7,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":8,\"name\":\"TestFilm8\",\"description\":\"TestDescription8\"," +
                        "\"releaseDate\":\"1900-01-09\",\"duration\":9,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":3,\"name\":\"TestFilm3\",\"description\":\"TestDescription3\"," +
                        "\"releaseDate\":\"1900-01-04\",\"duration\":4,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}]"));
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 11 самых популярных фильмов если передан параметр count=11")
    void getListOfMostPopularFilmsTest_ReturnsList11MostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular?count=11");
        Mockito.when(filmLikeService.getMostPopularFilm(11)).thenReturn(List.of(
                Film.builder()
                        .id(1)
                        .name("TestFilm1")
                        .description("TestDescription1")
                        .releaseDate(LocalDate.parse("1900-01-02"))
                        .duration(2)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(11)
                        .name("TestFilm11")
                        .description("TestDescription11")
                        .releaseDate(LocalDate.parse("1900-01-12"))
                        .duration(12)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(4)
                        .name("TestFilm4")
                        .description("TestDescription4")
                        .releaseDate(LocalDate.parse("1900-01-05"))
                        .duration(5)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(5).name("TestFilm5")
                        .description("TestDescription5")
                        .releaseDate(LocalDate.parse("1900-01-06"))
                        .duration(6)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(7)
                        .name("TestFilm7")
                        .description("TestDescription7")
                        .releaseDate(LocalDate.parse("1900-01-08"))
                        .duration(8)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(9)
                        .name("TestFilm9")
                        .description("TestDescription9")
                        .releaseDate(LocalDate.parse("1900-01-10"))
                        .duration(10)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(10)
                        .name("TestFilm10")
                        .description("TestDescription10")
                        .releaseDate(LocalDate.parse("1900-01-11"))
                        .duration(11)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(6)
                        .name("TestFilm6")
                        .description("TestDescription6")
                        .releaseDate(LocalDate.parse("1900-01-07"))
                        .duration(7)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(8)
                        .name("TestFilm8")
                        .description("TestDescription8")
                        .releaseDate(LocalDate.parse("1900-01-09"))
                        .duration(9)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(3)
                        .name("TestFilm3")
                        .description("TestDescription3")
                        .releaseDate(LocalDate.parse("1900-01-04"))
                        .duration(4)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build(),
                Film.builder()
                        .id(2)
                        .name("TestFilm2")
                        .description("TestDescription2")
                        .releaseDate(LocalDate.parse("1900-01-03"))
                        .duration(3)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build()
        ));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"1900-01-02\",\"duration\":2,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":11,\"name\":\"TestFilm11\",\"description\":\"TestDescription11\"," +
                        "\"releaseDate\":\"1900-01-12\",\"duration\":12,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":4,\"name\":\"TestFilm4\",\"description\":\"TestDescription4\"," +
                        "\"releaseDate\":\"1900-01-05\",\"duration\":5,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":5,\"name\":\"TestFilm5\",\"description\":\"TestDescription5\"," +
                        "\"releaseDate\":\"1900-01-06\",\"duration\":6,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":7,\"name\":\"TestFilm7\",\"description\":\"TestDescription7\"," +
                        "\"releaseDate\":\"1900-01-08\",\"duration\":8,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":9,\"name\":\"TestFilm9\",\"description\":\"TestDescription9\"," +
                        "\"releaseDate\":\"1900-01-10\",\"duration\":10,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":10,\"name\":\"TestFilm10\",\"description\":\"TestDescription10\"," +
                        "\"releaseDate\":\"1900-01-11\",\"duration\":11,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":6,\"name\":\"TestFilm6\",\"description\":\"TestDescription6\"," +
                        "\"releaseDate\":\"1900-01-07\",\"duration\":7,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":8,\"name\":\"TestFilm8\",\"description\":\"TestDescription8\"," +
                        "\"releaseDate\":\"1900-01-09\",\"duration\":9,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":3,\"name\":\"TestFilm3\",\"description\":\"TestDescription3\"," +
                        "\"releaseDate\":\"1900-01-04\",\"duration\":4,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}," +
                        "{\"id\":2,\"name\":\"TestFilm2\",\"description\":\"TestDescription2\"," +
                        "\"releaseDate\":\"1900-01-03\",\"duration\":3,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}]"));
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 1 самых популярных фильмов если передан параметр count=1")
    void getListOfMostPopularFilmsTest_ReturnsList1MostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular?count=1");

        Mockito.when(filmLikeService.getMostPopularFilm(1)).thenReturn(List.of(
                Film.builder()
                        .id(1)
                        .name("TestFilm1")
                        .description("TestDescription1")
                        .releaseDate(LocalDate.parse("1900-01-02"))
                        .duration(2)
                        .genres(Set.of(new Genre(1, "G")))
                        .mpa(new Rating(1, "Комедия"))
                        .build()));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"1900-01-02\",\"duration\":2,\"mpa\":{\"id\":1,\"name\":\"Комедия\"}," +
                        "\"genres\":[{\"id\":1,\"name\":\"G\"}]}]"));
    }
}