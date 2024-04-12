package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {
    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

    void deleteUser(int id);
}
