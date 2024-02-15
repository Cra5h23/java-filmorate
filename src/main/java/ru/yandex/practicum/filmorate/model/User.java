package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User
 *
 * @author Nikolay Radivon
 */
@Data
@Builder(toBuilder = true)
public class User {
    /**
     * Индификатор пользователя
     */
    @Builder.Default
    private int id = 0;

    /**
     * Электронная почта
     */
    @NotNull
    @Email
    private String email;

    /**
     * Логин пользователя
     */
    @NotBlank(message = "Логин пользователя не должен быть пустым")
    private String login;

    /**
     * Имя пользователя(для отображения)
     */
    private String name;

    /**
     * Дата рождения
     */
    @NotNull
    @Past(message = "Дата рождения не должна быть в будущем")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer friendId) {
        friends.add(friendId);
    }
}
