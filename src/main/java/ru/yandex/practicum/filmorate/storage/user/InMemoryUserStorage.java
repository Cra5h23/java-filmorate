package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserStorageException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        var userId = user.getId();
        var u = checkUser(userId);
        u.setName(user.getName());
        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        log.info("Обновлён пользователь с id: {}", userId);
        return u;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public User getUserById(int id) {
        var user = checkUser(id);
        log.info("Запрошен пользователь с id: {}", id);
        return user;
    }

    /**
     * @param id
     */
    @Override
    public String deleteUser(int id) {
        checkUser(id);
        log.info("Удалён пользователь с id: {}", id);
        userMap.remove(id);
        return String.format("Удалён пользователь с id: %d", id);
    }

    public User checkUser(Integer id) {
        if (!userMap.containsKey(id)) {
            log.warn("Пользователь с id: {} не существует", id);
            throw new UserStorageException(String.format("Пользователь с id: %d не существует", id));
        }
        return userMap.get(id);
    }
}
