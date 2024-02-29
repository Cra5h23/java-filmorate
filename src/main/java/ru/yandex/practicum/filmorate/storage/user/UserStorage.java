package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user);

    Optional<User> getUserById(int id);

    void deleteUser(int id);
}
