package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private int generatorUserId = 0;

    /**
     * @return
     */
    @Override
    public Collection<User> getUsers() {
        log.info("Запрошен список всех пользователей");
        return userStorage.getAllUsers();
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User addUser(User user) {
        user.setId(++generatorUserId);
        userStorage.addUser(user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        var userId = user.getId();
        var u = checkUser(userId, "обновить");

        u.setName(user.getName());
        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        userStorage.updateUser(u);
        log.info("Обновлён пользователь с id: {}", userId);
        return u;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Integer userId) {
        log.info("Запрошен пользователь с id {}", userId);
        return checkUser(userId, "получить");
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public String deleteUserById(Integer userId) {
        checkUser(userId, "удалить");
        userStorage.deleteUser(userId);
        return format("Удалён пользователь с id: %d", userId);
    }

    public User checkUser(Integer userId, String s) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserServiceException(
                        format("Попытка %s пользователя с несуществующим id: %d", s, userId)));
    }
}
