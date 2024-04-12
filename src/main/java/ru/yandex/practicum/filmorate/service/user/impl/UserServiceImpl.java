package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    /**
     * @return
     */
    @Override
    public Collection<User> getUsers() {
        log.info("Запрошен список всех пользователей");
        return userDao.getAllUsers();
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User addUser(User user) {
        var u = userDao.addUser(user);
        log.info("Добавлен пользователь {}", u);
        return u;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        checkUser(user.getId(), "обновить");
        log.info("Обновлён пользователь с id: {}", user.getId());
        return userDao.updateUser(user);
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
        userDao.deleteUser(userId);
        log.info("Удалён пользователь с id: {}", userId);
        return format("Удалён пользователь с id: %d", userId);
    }

    public User checkUser(Integer userId, String s) {
        return userDao.getUserById(userId)
                .orElseThrow(() -> new UserServiceException(
                        format("Попытка %s пользователя с несуществующим id: %d", s, userId)));
    }
}
