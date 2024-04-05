package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.MinReleaseData;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 *
 * @author Nikolay Radzivon
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    /**
     * Идентификатор фильма
     */
    @Builder.Default
    private int id = 0;

    /**
     * Имя фильма
     */
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;

    /**
     * Описание фильма
     */
    @Size(max = 200, min = 1, message = "Описание фильма не должно быть меньше {min} и больше {max}")
    private String description;

    /**
     * Дата релиза фильма
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @MinReleaseData("1895-12-28")
    private LocalDate releaseDate;

    /**
     * Продолжительнность фильма
     */
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    /**
     * Рейтинг фильма
     */
    @Builder.Default
    private Rating mpa = new Rating();

    /**
     * Список жанров фильма
     */
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    /**
     * Список всех режссёров
     */
    @Builder.Default
    private List<Director> directors = new ArrayList<>();
}
