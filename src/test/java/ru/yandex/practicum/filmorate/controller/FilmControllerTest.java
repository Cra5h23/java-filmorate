package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InMemoryFilmStorage filmStorage;

    @Autowired
    InMemoryUserStorage userStorage;

    @Autowired
    FilmService filmService;

    @Test
    @DisplayName("GET /films возвращает коллекцию из двух фильмов")
    void getAllFilmsTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(2));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1" +
                        ",\"name\":\"TestFilm1\"" +
                        ",\"description\":\"TestDescription1\"" +
                        ",\"releaseDate\":\"1900-01-02\"" +
                        ",\"duration\":2" +
                        ",\"likes\":[]}" +
                        ",{\"id\":2" +
                        ",\"name\":\"TestFilm2\"" +
                        ",\"description\":\"TestDescription2\"" +
                        ",\"releaseDate\":\"1900-01-03\"" +
                        ",\"duration\":3" +
                        ",\"likes\":[]}]"
                )
        );

    }

    @Test
    @DisplayName("GET /films/1 возвращает фильм")
    void getUserById_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");
        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1" +
                        ",\"name\":\"TestFilm1\"" +
                        ",\"description\":\"TestDescription1\"" +
                        ",\"releaseDate\":\"1900-01-02\"" +
                        ",\"duration\":2" +
                        ",\"likes\":[]}"
                ));
    }

    @Test
    @DisplayName("GET /films/1 возвращает статус 404 и тело ошибки")
    void getUserById_ReturnsNotValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка получения фильма\":\"Фильм с id: 1 не существует\"}"),
                jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("POST /films создаёт новый фильм, возвращает статус 201")
    void addNewFilmTestValid() throws Exception {
        var requestBuilder = post("/films")
                .contentType(APPLICATION_JSON).content("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":120})");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isCreated(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":120,\"likes\":[]})"),
                jsonPath("$.id").exists());
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
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"TestFilm1Update\"," +
                        "\"description\":\"TestDescription1update\"," +
                        "\"releaseDate\":\"1980-10-10\"," +
                        "\"duration\":200})");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1," +
                        "\"name\":\"TestFilm1Update\"," +
                        "\"description\":\"TestDescription1update\"," +
                        "\"releaseDate\":\"1980-10-10\"" +
                        ",\"duration\":200," +
                        "\"likes\":[]}")
        );
    }

    @Test
    @DisplayName("PUT /films не обновляет фильм если передан фильм с пустым именем " +
            "и возвращает код 400 и сообщение ошибки в тело ответа")
    void updateFilmTestNotValidName() throws Exception {
        var requestBuilder = put("/films")
                .contentType(APPLICATION_JSON).content("{\"id\":1,\"name\":\"\"," +
                        "\"description\":\"TestDescription1update\"," +
                        "\"releaseDate\":\"1980-10-10\"," +
                        "\"duration\":200})");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
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

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
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

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
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

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
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

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().string("Удалён фильм с id: 1")
        );
    }

    @Test
    @DisplayName("DELETE /films/{id} не удаляет фильм с заданным ид если он не существует и возвращает код 404")
    void deleteFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = delete("/films/1");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка получения фильма\":\"Фильм с id: 1 не существует\"}")
                , jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} устанавливает лайк фильму с заданым filmId " +
            "от пользователя с заданным userId и возвращает статус 200")
    void userLikesFilmTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = put("/films/1/like/1");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
        this.userStorage.getUserMap().putAll(generatorUserMap(1));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Пользователь с id:1 поставил лайк фильму с id:1")
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} не устанавливает лайк фильму " +
            "с не существующим filmId от пользователя с заданным userId и выдаёт в ответ код 404 и сообщение ошибки")
    void userLikesFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = put("/films/1/like/1");

        this.userStorage.getUserMap().putAll(generatorUserMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка установки лайка\":" +
                        "\"Попытка добавить лайк фильму с несуществующим id:1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("PUT /films/{filmId}/like/{userId} не устанавливает лайк фильму " +
            "с заданным filmId от пользователя с не существующим userId и выдаёт в ответ код 404 и сообщение ошибки")
    void userLikesFilmTest_NotExistsUser() throws Exception {
        var requestBuilder = put("/films/1/like/1");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка установки лайка\":" +
                        "\"Попытка добавить лайк фильму от несуществующего пользователя c id:1\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} удаляет лайк у заданного фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTestValid() throws Exception {
        var requestBuilder = delete("/films/1/like/1");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
        this.userStorage.getUserMap().putAll(generatorUserMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().string("Пользователь с id:1 удалил лайк у фильма с id:1")
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} не удаляет лайк у несуществующего фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTest_NotExistsFilm() throws Exception {
        var requestBuilder = delete("/films/1/like/1");

        this.userStorage.getUserMap().putAll(generatorUserMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                , content().json("{\"Ошибка установки лайка\":" +
                        "\"Попытка удалить лайк у фильма с несуществующим id:1\"}")
                , jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("DELETE /films/{filmId}/like/{userId} не удаляет лайк у несуществующего фильма с filmId " +
            "от заданного пользователя с userId")
    void userRemoveLikeFromFilmTest_NotExistsUser() throws Exception {
        var requestBuilder = delete("/films/1/like/1");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(1));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound()
                , content().json("{\"Ошибка установки лайка\":" +
                        "\"Попытка удалить лайк у фильма от несуществующего пользователя c id:1\"}")
                , jsonPath("$.timestamp").exists()
        );
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 10 самых популярных фильмов по умолчанию")
    void getListOfMostPopularFilmsTest_ReturnsDefaultListMostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(11));
        this.userStorage.getUserMap().putAll(generatorUserMap(10));
        IntStream.range(1, 11).forEach(i -> this.filmService.addLikeFilm(1, i));
        IntStream.range(1, 1).forEach(i -> this.filmService.addLikeFilm(2, i));
        IntStream.range(1, 3).forEach(i -> this.filmService.addLikeFilm(3, i));
        IntStream.range(3, 11).forEach(i -> this.filmService.addLikeFilm(4, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(5, i));
        IntStream.range(4, 8).forEach(i -> this.filmService.addLikeFilm(6, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(7, i));
        IntStream.range(5, 8).forEach(i -> this.filmService.addLikeFilm(8, i));
        IntStream.range(2, 8).forEach(i -> this.filmService.addLikeFilm(9, i));
        IntStream.range(6, 11).forEach(i -> this.filmService.addLikeFilm(10, i));
        IntStream.range(1, 10).forEach(i -> this.filmService.addLikeFilm(11, i));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().contentType(APPLICATION_JSON)
                , content().json(
                        "[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\",\"releaseDate\":\"1900-01-02\",\"duration\":2,\"likes\":[1,2,3,4,5,6,7,8,9,10]}" +
                                ",{\"id\":11,\"name\":\"TestFilm11\",\"description\":\"TestDescription11\",\"releaseDate\":\"1900-01-12\",\"duration\":12,\"likes\":[1,2,3,4,5,6,7,8,9]}" +
                                ",{\"id\":4,\"name\":\"TestFilm4\",\"description\":\"TestDescription4\",\"releaseDate\":\"1900-01-05\",\"duration\":5,\"likes\":[3,4,5,6,7,8,9,10]}" +
                                ",{\"id\":5,\"name\":\"TestFilm5\",\"description\":\"TestDescription5\",\"releaseDate\":\"1900-01-06\",\"duration\":6,\"likes\":[1,2,3,4,5,6,7]}" +
                                ",{\"id\":7,\"name\":\"TestFilm7\",\"description\":\"TestDescription7\",\"releaseDate\":\"1900-01-08\",\"duration\":8,\"likes\":[1,2,3,4,5,6,7]}" +
                                ",{\"id\":9,\"name\":\"TestFilm9\",\"description\":\"TestDescription9\",\"releaseDate\":\"1900-01-10\",\"duration\":10,\"likes\":[2,3,4,5,6,7]}" +
                                ",{\"id\":10,\"name\":\"TestFilm10\",\"description\":\"TestDescription10\",\"releaseDate\":\"1900-01-11\",\"duration\":11,\"likes\":[6,7,8,9,10]}" +
                                ",{\"id\":6,\"name\":\"TestFilm6\",\"description\":\"TestDescription6\",\"releaseDate\":\"1900-01-07\",\"duration\":7,\"likes\":[4,5,6,7]}" +
                                ",{\"id\":8,\"name\":\"TestFilm8\",\"description\":\"TestDescription8\",\"releaseDate\":\"1900-01-09\",\"duration\":9,\"likes\":[5,6,7]}" +
                                ",{\"id\":3,\"name\":\"TestFilm3\",\"description\":\"TestDescription3\",\"releaseDate\":\"1900-01-04\",\"duration\":4,\"likes\":[1,2]}]"
                ));
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 11 самых популярных фильмов если передан параметр count=11")
    void getListOfMostPopularFilmsTest_ReturnsList11MostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular?count=11");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(11));
        this.userStorage.getUserMap().putAll(generatorUserMap(10));
        IntStream.range(1, 11).forEach(i -> this.filmService.addLikeFilm(1, i));
        IntStream.range(1, 1).forEach(i -> this.filmService.addLikeFilm(2, i));
        IntStream.range(1, 3).forEach(i -> this.filmService.addLikeFilm(3, i));
        IntStream.range(3, 11).forEach(i -> this.filmService.addLikeFilm(4, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(5, i));
        IntStream.range(4, 8).forEach(i -> this.filmService.addLikeFilm(6, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(7, i));
        IntStream.range(5, 8).forEach(i -> this.filmService.addLikeFilm(8, i));
        IntStream.range(2, 8).forEach(i -> this.filmService.addLikeFilm(9, i));
        IntStream.range(6, 11).forEach(i -> this.filmService.addLikeFilm(10, i));
        IntStream.range(1, 10).forEach(i -> this.filmService.addLikeFilm(11, i));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().contentType(APPLICATION_JSON)
                , content().json(
                        "[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\"" +
                                ",\"releaseDate\":\"1900-01-02\",\"duration\":2,\"likes\":[1,2,3,4,5,6,7,8,9,10]}" +
                                ",{\"id\":11,\"name\":\"TestFilm11\",\"description\":\"TestDescription11\"" +
                                ",\"releaseDate\":\"1900-01-12\",\"duration\":12,\"likes\":[1,2,3,4,5,6,7,8,9]}" +
                                ",{\"id\":4,\"name\":\"TestFilm4\",\"description\":\"TestDescription4\"" +
                                ",\"releaseDate\":\"1900-01-05\",\"duration\":5,\"likes\":[3,4,5,6,7,8,9,10]}" +
                                ",{\"id\":5,\"name\":\"TestFilm5\",\"description\":\"TestDescription5\"" +
                                ",\"releaseDate\":\"1900-01-06\",\"duration\":6,\"likes\":[1,2,3,4,5,6,7]}" +
                                ",{\"id\":7,\"name\":\"TestFilm7\",\"description\":\"TestDescription7\"" +
                                ",\"releaseDate\":\"1900-01-08\",\"duration\":8,\"likes\":[1,2,3,4,5,6,7]}" +
                                ",{\"id\":9,\"name\":\"TestFilm9\",\"description\":\"TestDescription9\"" +
                                ",\"releaseDate\":\"1900-01-10\",\"duration\":10,\"likes\":[2,3,4,5,6,7]}" +
                                ",{\"id\":10,\"name\":\"TestFilm10\",\"description\":\"TestDescription10\"" +
                                ",\"releaseDate\":\"1900-01-11\",\"duration\":11,\"likes\":[6,7,8,9,10]}" +
                                ",{\"id\":6,\"name\":\"TestFilm6\",\"description\":\"TestDescription6\"" +
                                ",\"releaseDate\":\"1900-01-07\",\"duration\":7,\"likes\":[4,5,6,7]}" +
                                ",{\"id\":8,\"name\":\"TestFilm8\",\"description\":\"TestDescription8\"" +
                                ",\"releaseDate\":\"1900-01-09\",\"duration\":9,\"likes\":[5,6,7]}" +
                                ",{\"id\":3,\"name\":\"TestFilm3\",\"description\":\"TestDescription3\"" +
                                ",\"releaseDate\":\"1900-01-04\",\"duration\":4,\"likes\":[1,2]}" +
                                ",{\"id\":2,\"name\":\"TestFilm2\",\"description\":\"TestDescription2\"" +
                                ",\"releaseDate\":\"1900-01-03\",\"duration\":3,\"likes\":[]}]"
                ));
    }

    @Test
    @DisplayName("GET /films/popular возвращает список из 1 самых популярных фильмов если передан параметр count=1")
    void getListOfMostPopularFilmsTest_ReturnsList1MostPopularFilms() throws Exception {
        var requestBuilder = get("/films/popular?count=1");

        this.filmStorage.getFilmMap().putAll(generatorFilmMap(11));
        this.userStorage.getUserMap().putAll(generatorUserMap(10));
        IntStream.range(1, 11).forEach(i -> this.filmService.addLikeFilm(1, i));
        IntStream.range(1, 1).forEach(i -> this.filmService.addLikeFilm(2, i));
        IntStream.range(1, 3).forEach(i -> this.filmService.addLikeFilm(3, i));
        IntStream.range(3, 11).forEach(i -> this.filmService.addLikeFilm(4, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(5, i));
        IntStream.range(4, 8).forEach(i -> this.filmService.addLikeFilm(6, i));
        IntStream.range(1, 8).forEach(i -> this.filmService.addLikeFilm(7, i));
        IntStream.range(5, 8).forEach(i -> this.filmService.addLikeFilm(8, i));
        IntStream.range(2, 8).forEach(i -> this.filmService.addLikeFilm(9, i));
        IntStream.range(6, 11).forEach(i -> this.filmService.addLikeFilm(10, i));
        IntStream.range(1, 10).forEach(i -> this.filmService.addLikeFilm(11, i));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                , content().contentType(APPLICATION_JSON)
                , content().json(
                        "[{\"id\":1,\"name\":\"TestFilm1\",\"description\":\"TestDescription1\"" +
                                ",\"releaseDate\":\"1900-01-02\",\"duration\":2,\"likes\":[1,2,3,4,5,6,7,8,9,10]}]"
                ));
    }


    private Map<Integer, Film> generatorFilmMap(int filmQuantity) {
        Map<Integer, Film> filmMap = new HashMap<>();
        for (int i = 1; i <= filmQuantity; i++) {
            filmMap.put(i, new Film(i, "TestFilm" + i, "TestDescription" + i,
                    LocalDate.of(1900, 1, 1).plusDays(i), i + 1));
        }
        return filmMap;
    }

    private Map<Integer, User> generatorUserMap(int userQuantity) {
        Map<Integer, User> userMap = new HashMap<>();
        for (int i = 1; i <= userQuantity; i++) {
            userMap.put(i, new User(i, "testEmail@tesr.com" + i, "testLogin" + i, "testName" + i,
                    LocalDate.of(1990, 1, 1).plusDays(i)));
        }
        return userMap;
    }
}
