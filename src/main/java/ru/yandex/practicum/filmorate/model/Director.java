package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Nikolay Radzivon
 */
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Director {

    private Integer id;
    @NotBlank(message = "Имя режиcсёра не должно быть пустым")
    private String name;
}
