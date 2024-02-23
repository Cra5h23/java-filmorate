package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Integer userId);

    String deleteUserById(Integer userId);
}
