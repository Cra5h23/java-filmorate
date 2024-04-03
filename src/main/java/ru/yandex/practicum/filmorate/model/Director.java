package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author Nikolay Radzivon
 */
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
public class Director {
    private Integer id;
    @NotBlank(message = "Имя режесёра не должно быть пустым")
    private String name;
}
