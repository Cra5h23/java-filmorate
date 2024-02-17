package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.exeption.UserStorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Nikolay Radzivon
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Метод добавления пользователя в друзья
     *
     * @param userId
     * @param friendId
     * @return
     */
    public String addingUserAsFriend(int userId, int friendId) {
        var user = checkUser(userId,"добавить друга для");
        var friend = checkUser(friendId,"добавить в друзья");
        user.addFriend(friendId);
        friend.addFriend(userId);
        return String.format("Пользователь с id:%d добавил в друзья пользователя с id: %d", userId, friendId);

//        User friend;
//        try {
//           friend = userStorage.getUserById(friendId);
//        } catch (ValidationException e) {
//            throw new UserServiceException(format("Попытка добавить в друзья несуществующего пользователя с id: %d", friendId));
//        }



//        if (userStorage.getUserById(userId) == null) {
//            log.info("Нет юзера");
//            throw new UserServiceException(format("Пользователь с id: %d не существует", userId)); //todo вынести в общий метод?
//        }
//        /*if (userStorage.getUserById(friendId) == null) {
//            throw new UserServiceException(format("Попытка добавить в друзья несуществующего пользователя с id: %d", friendId));
//        }*/
//        userStorage.getUserById(userId).addFriend(friendId);
//        userStorage.getUserById(friendId).addFriend(userId);
//        return String.format("Пользователь с id:%d добавил в друзья пользователя с id: %d", userId, friendId);
    }

    public String deletingFromUserFriends(int userId, int deletingFriendId) {
        var user = checkUser(userId,"удалить друга для");
        var deletingFriend = checkUser(deletingFriendId,"удалить из друзей");
        user.deletingFriend(deletingFriendId);
        deletingFriend.deletingFriend(userId);
        return String.format("Пользователь с id: %d удалил из друзей пользователя с id: %d", userId, deletingFriendId);

//        User deletingFriend;
//        try {
//            deletingFriend = userStorage.getUserById(deletingFriendId);
//        } catch (ValidationException e) {
//            throw new UserServiceException(format("Попытка удалить из друзей несуществующего пользователя с id: %d", deletingFriendId));
//        }


//        /*if (userStorage.getUserById(userId) == null) {
//            throw new UserServiceException(format("Пользователь с id: %d не существует", userId));
//        }*/
//        if (userStorage.getUserById(deletingFriendId) == null) {
//            throw new UserServiceException(format("Попытка удалить из друзей несуществующего пользователя с id: %d", deletingFriendId));
//        }
//        userStorage.getUserById(userId).getFriends().remove(deletingFriendId);
//        userStorage.getUserById(deletingFriendId).getFriends().remove(userId);
//        return String.format("Пользователь с id: %d удалил из друзей пользователя с id: %d", userId, deletingFriendId);
    }

    public Collection<User> getUserFriends(int userId) {
        var user = checkUser(userId, "получить список друзей для");
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());


//        /*if (userStorage.getUserById(userId) == null) {
//            throw new ValidationException(format("Пользователь с id: %d не существует", userId));
//        }*/
//        return userStorage.getUserById(userId).getFriends().stream()
//                .map(userStorage::getUserById)
//                .collect(Collectors.toList());
    }

    public Collection<User> getListOfCommonFriends(int userId, int otherUserId) {
        var user = checkUser(userId,"получить список общих друзей для");
        var otherUser = checkUser(otherUserId,"получить список общих друзей");
        return user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

//        User otherUser;
//        try {
//            otherUser = userStorage.getUserById(otherUserId);
//        } catch (ValidationException e) {
//            throw new ValidationException(format("Пользователь с id: %d, с которым вы хотите сравнить друзей не существует", otherUserId));
//        }



//        /*if (userStorage.getUserById(userId) == null) {
//            throw new ValidationException(format("Пользователь с id: %d не существует", userId));
//        }*/
//        if (userStorage.getUserById(otherUserId) == null) {
//            throw new ValidationException(format("Пользователь с которым вы хотите сравнить друзей не существует id: %d", otherUserId));
//        }
//
//        return userStorage.getUserById(userId).getFriends().stream()
//                .filter(id -> userStorage.getUserById(otherUserId).getFriends().contains(id))
//                .map(userStorage::getUserById)
//                .collect(Collectors.toList());
    }

    // Методы которые должны быть
    // добавление в друзья
    // удаление из друзей
    // вывод списка общих друзей.
}
