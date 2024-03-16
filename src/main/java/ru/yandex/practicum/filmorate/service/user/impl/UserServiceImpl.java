package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    //Todo убрать генерацию ид в хранилище
    //private int generatorUserId = 0;

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
        //user.setId(++generatorUserId);
        var u = userStorage.addUser(user);
        log.info("Добавлен пользователь {}", u);
        return u;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        //var userId = user.getId();
        checkUser(user.getId(), "обновить");

//        u.setName(user.getName());
//        u.setLogin(user.getLogin());
//        u.setEmail(user.getEmail());
//        u.setBirthday(user.getBirthday());
        log.info("Обновлён пользователь с id: {}", user.getId());
        return userStorage.updateUser(user);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Integer userId) {
        log.info("Запрошен пользователь с id {}", userId);
        //return userStorage.getUserById(userId);
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
