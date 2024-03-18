# java-filmorate
Template repository for Filmorate project.
# Стуруктура базы данных для приложения Filmorate.
## Содержание:
1. [Схема базы данных.]

2. [Краткое описание всех таблиц.]

3. [Примеры запросов.]

### Схема базы данных.

![Sprint11DataBAse drawio (2)](https://github.com/Cra5h23/java-filmorate/assets/145023705/d0bdb2d4-cf4d-4073-9655-e60b4f14e9ff)


### Краткое описание всех таблиц.
- #### Таблица films используется для хранения всех фильмов.
    * Поле film_id первичный ключ таблицы, содержит идентификационный номер фильма.
    * Поле rating_id внешний ключ используется для связи с таблицей rating, содержит идентификационный номер рейтинга.
    * Поле name содержит название фильма.
    * Поле description содержит описание фильма.
    * Поле release_date содержит дату релиза фильма.
    * Поле duration содержит продолжительность фильма.

- #### Таблица users используется для хранения всех пользователей.
    * Поле user_id первичный ключ таблицы, содержит идентификационный номер пользователя.
    * Поле email содержит email пользователя.
    * Поле login содержит логин пользователя.
    * Поле name содержит имя пользователя.
    * Поле birthday содержит день рождения пользователя.

- #### Таблица likes используется для хранения лайков которые поставили пользователи фильмам.
    * Поле film_id идентификационный номер фильма.
    * Поле user_id идентификационный номер пользователя.

- #### Таблица film_ganre используется для хранения жанров которым принадлежит фильм.
    * Поле film_id идентификационный номер фильма.
    * Поле genre_id идентификационный номер жанра.

- ##### Таблица genres используется для хранения названий жанров.
    * Поле genre_id идентификационный номер жанра.
    * Поле genre_name название жанра.

- ##### Таблица ratings используется для хранения названий рейтингов.
    * Поле rating_id первичный ключ таблицы, содержит идентификационный номер рейтинга.
    * Поле rating_name содержит название рейтинга.

- ##### Таблица friends используется для хранения друзей пользователей.
    * Поле user_id идентификационный номер пользователя.
    * Поле friend_id идентификационный номер друга.
    * Поле status хранит статус запроса дружбы


### Примеры запросов.
- #### Получение всех фильмов.
  ```SQL
  SELECT
  f.*,
  STRING_AGG(fg.GENRE_ID, ', ') genres,
  STRING_AGG(l.USER_ID, ', ') likes
  FROM FILMS f
  LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID
  LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID
  GROUP BY f.FILM_ID
  ```
- #### Получение фильма по id.
  ```SQL
  SELECT
  f.*,
  STRING_AGG(fg.genre_id, ', ') genres,
  STRING_AGG(l.user_id, ', ') likes
  FROM films f 
  LEFT JOIN film_genres fg on f.film_id = fg.film_id
  LEFT JOIN likes l on f.film_id = l.film_id
  WHERE f.film_id = ? 
  GROUP BY f.film_id"
  ```
- #### Получение списка самых популярных фильмов, c колличеством фильмов count
  ```SQL
  SELECT
  f.*,
  STRING_AGG(fg.GENRE_ID, ', ') genres,
  STRING_AGG(l.USER_ID, ', ') likes
  FROM FILMS
  LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID
  LEFT JOIN LIKES l ON f.FILM_ID =l.FILM_ID
  GROUP BY f.FILM_ID
  ORDER BY count(l.user_id) DESC
  limit ?
  ```

- #### Получение всех пользователей.
  ```SQL
  SELECT
  a.*
  FROM (SELECT
  u.*,
  STRING_AGG(f.friend_id, ', ') friends
  FROM USERS u
  LEFT JOIN FRIENDS f ON u.user_id = f.user_id and f.status=true
  GROUP BY u.user_id ) a
  ```

- #### Получение пользователя по id.
  ```SQL
  SELECT
  a.*
  FROM (SELECT
  u.*,
  STRING_AGG(f.friend_id, ', ') friends
  FROM USERS u
  LEFT JOIN FRIENDS f ON u.user_id = f.user_id and f.status=true
  GROUP BY u.user_id) a
  WHERE user_id = ?
  ```
