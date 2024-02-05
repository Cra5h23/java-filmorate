package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FilmController filmController;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        filmController.getFilmMap().clear();
    }

    @Test
    @DisplayName("GET localhost:8080/films возвращает коллекцию из двух фильмов")
    void getAllFilmsTest() throws Exception {
        var requestBuilder = get("http://localhost:8080/films");
        Film film1Test = Film.builder()
                .name("TestFilm1")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film2Test = Film.builder()
                .name("TestFilm2")
                .duration(30)
                .description("Test2")
                .releaseDate(LocalDate.now().minusDays(30))
                .build();
        List<Film> filmList = List.of(this.filmController.addNewFilm(film1Test), this.filmController.addNewFilm(film2Test));

        String s = objectMapper.writeValueAsString(filmList);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(s)
        );
    }

    @Test
    @DisplayName("POST localhost:8080/films создаёт новый фильм")
    void addNewFilmTestValid() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm1")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        String s = objectMapper.writeValueAsString(film1);
        String s1 = objectMapper.writeValueAsString(film1.toBuilder().id(1).build());
        var requestBuilder = post("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(s1)
        );
    }

    @Test
    @DisplayName("POST localhost:8080/films не создаёт фильм с пустым именем")
    void addNewFilmTestNotValidName() throws Exception {
        Film film1 = Film.builder()
                .name("")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        String s = objectMapper.writeValueAsString(film1);
        var requestBuilder = post("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("POST localhost:8080/films не создаёт фильм с длинной описание больше 200 символов")
    void addNewFilmTestNotValidDescriptionLength() throws Exception {
        Film film1 = Film.builder()
                .name("Test1")
                .duration(20)
                .description("SE3Qwr9lTHLmMflOygSaJ7iPcfmiwHUf4qRGW754wRYQxvl1B31DVxo5jqHpEEMEo" +
                        "wYiSt0OoVdQCGDogHdl7j5AsgF94YaSBzy7te3LJ29abX162KtDKftk3DV3qExTQ9jOPP1zO1LZGg6dMNKuMQuK3FYI" +
                        "00QkByZmmHQd64jylIhKVEcBj13DpgFhJwBtCrcZslVOU")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        String s = objectMapper.writeValueAsString(film1);
        var requestBuilder = post("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("POST localhost:8080/films не создаёт фильм если дата релиза раньше 28-12-1895")
    void addNewFilmTestNotValidReleaseData() throws Exception {
        Film film1 = Film.builder()
                .name("Test1")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.of(1894, 12, 23))
                .build();
        String s = objectMapper.writeValueAsString(film1);
        var requestBuilder = post("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("POST localhost:8080/films не создаёт фильм с отрицательной длительностью")
    void addNewFilmTestNotValidDuration() throws Exception {
        Film film1 = Film.builder()
                .name("Test1")
                .duration(-20)
                .description("Test1")
                .releaseDate(LocalDate.now())
                .build();
        String s = objectMapper.writeValueAsString(film1);
        var requestBuilder = post("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("PUT localhost:8080/films обновляет фильм")
    void updateFilmTestValid() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film = filmController.addNewFilm(film1);

        String s = objectMapper.writeValueAsString(film.toBuilder().name("TestFilmUpdate").build());

        var requestBuilder = put("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(s)
        );
    }

    @Test
    @DisplayName("PUT localhost:8080/films не обновляет фильм если передан фильм с пустым именем")
    void updateFilmTestNotValidName() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film = filmController.addNewFilm(film1);

        String s = objectMapper.writeValueAsString(film.toBuilder().name("").build());

        var requestBuilder = put("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("PUT localhost:8080/films не обновляет фильм если описание больше 200 символов")
    void updateFilmTestNotValidDescriptionLength() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film = filmController.addNewFilm(film1);

        String s = objectMapper.writeValueAsString(film.toBuilder()
                .description("SE3Qwr9lTHLmMflOygSaJ7iPcfmiwHUf4qRGW754wRYQxvl1B31DVxo5jqHpEEMEowYiSt0OoVdQCGDogHdl7j5As" +
                        "gF94YaSBzy7te3LJ29abX162KtDKftk3DV3qExTQ9jOPP1zO1" +
                        "LZGg6dMNKuMQuK3FYI00QkByZmmHQd64jylIhKVEcBj13DpgFhJwBtCrcZslVOU")
                .build());

        var requestBuilder = put("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("PUT localhost:8080/films не обновляет фильм если если дата релиза раньше 28-12-1895")
    void updateFilmTestNotValid() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film = filmController.addNewFilm(film1);

        String s = objectMapper.writeValueAsString(film.toBuilder().releaseDate(LocalDate.of(1894, 12, 23)).build());

        var requestBuilder = put("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    @DisplayName("PUT localhost:8080/films не обновляет фильм если передан фильм с отрицательной длительностью")
    void updateFilmTestNotValidDuration() throws Exception {
        Film film1 = Film.builder()
                .name("TestFilm")
                .duration(20)
                .description("Test1")
                .releaseDate(LocalDate.now().minusDays(20))
                .build();
        Film film = filmController.addNewFilm(film1);

        String s = objectMapper.writeValueAsString(film.toBuilder().duration(-20).build());

        var requestBuilder = put("http://localhost:8080/films")
                .contentType(APPLICATION_JSON).content(s);

        this.mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest()
        );
    }

}
