package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Getter
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();

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
     */
    @Override
    public void addUser(User user) {
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
    }

    /**
     * @param user
     */
    @Override
    public void updateUser(User user) { //todo проверить
        userMap.put(user.getId(), user);
        log.info("Обновлён пользователь с id: {}", user.getId());
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
