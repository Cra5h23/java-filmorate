package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exeption.GenreServiceException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    GenreService genreService;

    @Test
    @DisplayName("GET /genres возвращает список всех жанров")
    void getAllGenresTest() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/genres");

        Mockito.when(genreService.getAllGenre()).thenReturn(
                List.of(new Genre(1, "Комедия"),
                        new Genre(2, "Драма"),
                        new Genre(3, "Мультфильм"),
                        new Genre(4, "Триллер"),
                        new Genre(5, "Документальный"),
                        new Genre(6, "Боевик")));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("[{\"id\":1,\"name\":\"Комедия\"}," +
                        "{\"id\":2,\"name\":\"Драма\"}," +
                        "{\"id\":3,\"name\":\"Мультфильм\"}," +
                        "{\"id\":4,\"name\":\"Триллер\"}," +
                        "{\"id\":5,\"name\":\"Документальный\"}," +
                        "{\"id\":6,\"name\":\"Боевик\"}]")
        );
    }

    @Test
    @DisplayName("GET /genres/{genreId} возвращает жанр")
    void getGenreByIdTestReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/genres/1");

        Mockito.when(genreService.getGenreById(1)).thenReturn(new Genre(1, "Комедия"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("{\"id\":1,\"name\":\"Комедия\"}")
        );
    }

    @Test
    @DisplayName("GET /genres/{genreId} возвращает код 404 и сообщение ошибки если запрошен не существующий жанр")
    void getGenreByIdTestReturnsNotValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/genres/1");

        Mockito.when(genreService.getGenreById(1)).thenThrow(new GenreServiceException(
                "Запрашиваемый жанр с id 1 не существует"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("{\"Ошибка работы с жанрами\":\"Запрашиваемый жанр с id 1 не существует\"}"),
                jsonPath("$.timestamp").exists()
        );
    }
}


