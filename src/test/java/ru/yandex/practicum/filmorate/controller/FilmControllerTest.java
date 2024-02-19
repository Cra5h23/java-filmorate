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
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Map;

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
    UserService userService;

    @Autowired
    FilmService filmService;

    @Test
    @DisplayName("GET /films возвращает коллекцию из двух фильмов")
    void getAllFilmsTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films");
        this.filmStorage.getFilmMap().putAll(
                Map.of(1, new Film(1, "TestFilm1", "TestDescription1",
                                LocalDate.of(2000, 10, 10), 20),
                        2, new Film(2, "TestFilm2", "TestDescription2",
                                LocalDate.of(2021, 1, 10), 120)));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("[{\"id\":1," +
                        "\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":20,\"likes\":[]}," +
                        "{\"id\":2," +
                        "\"name\":\"TestFilm2\"," +
                        "\"description\":\"TestDescription2\"," +
                        "\"releaseDate\":\"2021-01-10\"," +
                        "\"duration\":120,\"likes\":[]}]"
                )
        );

    }

    @Test
    @DisplayName("GET /films/1 возвращает фильм")
    void getUserById_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");
        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"id\":1," +
                        "\"name\":\"TestFilm1\"," +
                        "\"description\":\"TestDescription1\"," +
                        "\"releaseDate\":\"2000-10-10\"," +
                        "\"duration\":20,\"likes\":[]})"
                ));
    }

    @Test
    @DisplayName("GET /films/1 возвращает статус 404 и тело ошибки")
    void getUserById_ReturnsNotValidResponseEntity() throws Exception {
        var requestBuilder = get("/films/1");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка получения фильма\":\"Фильм с id: 1 не существует\"}")
                , jsonPath("$.timestamp").exists());
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
                content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Название фильма не должно быть пустым\"}"),
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
                content().json("{\"Ошибка ввода данных\":\"Описание фильма не должно быть меньше 1 и больше 200\"}"),
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
                content().json("{\"Ошибка ввода данных\":\"Описание фильма не должно быть меньше 1 и больше 200\"}"),
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
                content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Дата релиза не должна быть раньше 1895-12-28\"}"),
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
                content().contentType(APPLICATION_JSON)
                , content().json("{\"Ошибка ввода данных\":\"Продолжительность фильма должна быть положительной\"}"),
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

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
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

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
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

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Описание фильма не должно быть меньше 1 и больше 200\"}"),
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

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
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

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(APPLICATION_JSON),
                content().json("{\"Ошибка ввода данных\":\"Продолжительность фильма должна быть положительной\"}"),
                jsonPath("$.timestamp").exists()
        );
    }

    @Test
    void deleteFilm_ReturnsValidResponseEntity() throws Exception{
        var requestBuilder = delete("/films/1");

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk()
                ,content().string("Удалён фильм с id: 1")
        );
    }

    @Test
    void deleteFilm_ReturnsNotValidResponseEntity() throws Exception{
        var requestBuilder = delete("/films/1");

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(APPLICATION_JSON)
                ,content().json("{\"Ошибка получения фильма\":\"Фильм с id: 1 не существует\"}")
                ,jsonPath("$.timestamp").exists()
        );
    }

    @Test
    void userLikesFilm() throws Exception{ //todo название теста
        var requestBuilder = put("/films/1/like/1");

        this.filmStorage.getFilmMap().put(1, new Film(1, "TestFilm1", "TestDescription1",
                LocalDate.of(2000, 10, 10), 20));
        this.userStorage.getUserMap().put(1, new User(1,"testEmail@test.com","testLogin", "testName",
                LocalDate.of(1990,11,22)));

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().string("Пользователь с id:1 поставил лайк фильму с id:1")

        );
    }
}
