package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = FilmController.class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/sql/films.sql"})
public class FilmControlleTests {
    @Autowired
    MockMvc mockMvc;

//    @Autowired
//    FilmController filmController;
//    @MockBean
//    FilmService filmService;
//    @MockBean
//    FilmLikeService filmLikeService;




    @Test
    @DisplayName("GET /films возвращает коллекцию из двух фильмов")
    //@Sql({"/schema.sql", "/sql/films.sql"})
    //@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    void getAllFilmsTest_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/films");
        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().json("sds")
        );
    }



    private List<Film> generatorFilmList(int count) {
        ArrayList<Film> films = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
           var f = Film.builder()
                   .id(i)
                   .name("testName" + i)
                   .description("testDescription"+i)
                   .mpa(new Mpa(1))
                   .duration(120+i)
                   .likes(Set.of())
                   .releaseDate(LocalDate.of(1989,5,1).plusDays(1))
                   .genres(List.of())
                   .build();
            films.add(f);
        }
        return films;
    }
}
