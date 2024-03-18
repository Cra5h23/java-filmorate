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
import ru.yandex.practicum.filmorate.exeption.RatingServiceException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RatingController.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RatingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RatingService ratingService;

    @Test
    @DisplayName("GET /genres возвращает список всех жанров")
    void getAllRatingsTest() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/mpa");

        Mockito.when(ratingService.getAllRatings()).thenReturn(List.of(
                new Rating(1, "G"),
                new Rating(2, "PG"),
                new Rating(3, "PG-13"),
                new Rating(4, "R"),
                new Rating(5, "NC-17")
        ));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("[{\"id\":1,\"name\":\"G\"}," +
                        "{\"id\":2,\"name\":\"PG\"}," +
                        "{\"id\":3,\"name\":\"PG-13\"}," +
                        "{\"id\":4,\"name\":\"R\"}," +
                        "{\"id\":5,\"name\":\"NC-17\"}]")
        );
    }

    @Test
    @DisplayName("GET mpa/{ratingId} возвращает рейтинг")
    void getRatingByIdReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/mpa/1");

        Mockito.when(ratingService.getRatingById(1)).thenReturn(new Rating(1, "G"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("{\"id\":1,\"name\":\"G\"}")
        );
    }

    @Test
    @DisplayName("GET mpa/{ratingId} возвращает код 404 и сообщение ошибки")
    void getRatingByIdReturnsNotValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/mpa/1");

        Mockito.when(ratingService.getRatingById(1)).thenThrow(new RatingServiceException(
                "Запрашиваемый рейтинг с id 1 не существует"));

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json("{\"Ошибка работы с рейтингами\":\"Запрашиваемый рейтинг с id 1 не существует\"}"),
                jsonPath("$.timestamp").exists()
        );
    }
}