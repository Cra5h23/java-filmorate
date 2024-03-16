package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Getter
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> userMap = new HashMap<>();
    private int generatorUserId = 0;
    /**
     * @return
     */
    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return Collections.unmodifiableCollection(userMap.values());
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User addUser(User user) {
        user.setId(++generatorUserId);
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    /**
     * @param user
     */
    @Override
    public User updateUser(User user) {
        var id = user.getId();
        var u = userMap.get(id);
        u.setName(user.getName());
        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        log.info("Обновлён пользователь с id: {}", user.getId());
        return u;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<User> getUserById(int id) {
        log.info("Запрошен пользователь с id: {}", id);
        return Optional.ofNullable(userMap.get(id));
    }

    /**
     * @param id
     */
    @Override
    public void deleteUser(int id) {
        log.info("Удалён пользователь с id: {}", id);
        userMap.remove(id);
    }
}
