# java-filmorate
Template repository for Filmorate project.
# Стуруктура базы данных для приложения Filmorate.
## Содержание:
1. [Схема базы данных.](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%81%D1%85%D0%B5%D0%BC%D0%B0-%D0%B1%D0%B0%D0%B7%D1%8B-%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85)

2. [Краткое описание всех таблиц.](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BA%D1%80%D0%B0%D1%82%D0%BA%D0%BE%D0%B5-%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D1%81%D0%B5%D1%85-%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86)
    - [Таблица films](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-films-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B2%D1%81%D0%B5%D1%85-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2)
    - [Таблица users](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-users-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B2%D1%81%D0%B5%D1%85-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B5%D0%B9)
    - [Таблица likes](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-likes-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BB%D0%B0%D0%B9%D0%BA%D0%BE%D0%B2-%D0%BA%D0%BE%D1%82%D0%BE%D1%80%D1%8B%D0%B5-%D0%BF%D0%BE%D1%81%D1%82%D0%B0%D0%B2%D0%B8%D0%BB%D0%B8-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B8-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%B0%D0%BC)
    - [Таблица film_ganre](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-film_ganre-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B6%D0%B0%D0%BD%D1%80%D0%BE%D0%B2-%D0%BA%D0%BE%D1%82%D0%BE%D1%80%D1%8B%D0%BC-%D0%BF%D1%80%D0%B8%D0%BD%D0%B0%D0%B4%D0%BB%D0%B5%D0%B6%D0%B8%D1%82-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC)
    - [Таблица genre](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-genre-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BD%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9-%D0%B6%D0%B0%D0%BD%D1%80%D0%BE%D0%B2)
    - [Таблица rating](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-rating-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BD%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9-%D1%80%D0%B5%D0%B9%D1%82%D0%B8%D0%BD%D0%B3%D0%BE%D0%B2)
    - [Таблица friends](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-friends-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B4%D1%80%D1%83%D0%B7%D0%B5%D0%B9-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B5%D0%B9)
    - [Таблица friend](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-friend-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%B0-%D0%B4%D0%BE%D0%B1%D0%B0%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F-%D0%B2-%D0%B4%D1%80%D1%83%D0%B7%D1%8C%D1%8F)
    - [Таблица status](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D1%82%D0%B0%D0%B1%D0%BB%D0%B8%D1%86%D0%B0-status-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D1%82%D1%81%D1%8F-%D0%B4%D0%BB%D1%8F-%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F-%D0%BD%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9-%D1%81%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%BE%D0%B2-%D0%B4%D1%80%D1%83%D0%B7%D0%B5%D0%B9)

3. [Примеры запросов.](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80%D1%8B-%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%BE%D0%B2)
    - [Получение всех фильмов](https://github.com/Cra5h23/Sprint11DataBase/blob/main/README.md#%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B2%D1%81%D0%B5%D1%85-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2)
    - [Получение фильма по id](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%B0-%D0%BF%D0%BE-id)
    - [Получение списка самых популярных фильмов, c колличеством фильмов count](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D1%81%D0%BF%D0%B8%D1%81%D0%BA%D0%B0-%D1%81%D0%B0%D0%BC%D1%8B%D1%85-%D0%BF%D0%BE%D0%BF%D1%83%D0%BB%D1%8F%D1%80%D0%BD%D1%8B%D1%85-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2-c-%D0%BA%D0%BE%D0%BB%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D1%82%D0%B2%D0%BE%D0%BC-%D1%84%D0%B8%D0%BB%D1%8C%D0%BC%D0%BE%D0%B2-count)
    - [Получение всех пользователей](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B2%D1%81%D0%B5%D1%85-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B5%D0%B9)
    - [Получение пользователя по id](https://github.com/Cra5h23/Sprint11DataBase/edit/main/README.md#%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8F-%D0%BF%D0%BE-id)

### Схема базы данных.

![Sprint11DataBase.png](resources/Sprint11DataBase.png)

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

- ##### Таблица genre используется для хранения названий жанров.
    * Поле genre_id идентификационный номер жанра.
    * Поле name название жанра.

- ##### Таблица rating используется для хранения названий рейтингов.
    * Поле rating_id первичный ключ таблицы, содержит идентификационный номер рейтинга.
    * Поле name содержит название рейтинга.

- ##### Таблица friends используется для хранения друзей пользователей.
    * Поле user_id идентификационный номер пользователя.
    * Поле friend_id идентификационный номер друга.

- ##### Таблица friend используется для хранения статуса добавления в друзья.
    * Поле	friend_id первичный ключ таблицы, содержит идентификационный номер друга.
    * Поле	user_id внешний ключ для связи с таблицей users.
    * Поле	status_id внешний ключ для связи с таблицей status.

- #### Таблица status используется для хранения названий статусов друзей.
    * Поле status_id первичный ключ таблицы.
    * Поле name название статуса.

### Примеры запросов.
- #### Получение всех фильмов.
  ```SQL
  SELECT *
  FROM films
  ```
- #### Получение фильма по id.
  ```SQL
  SELECT *
  FROM films f
  WHERE f.film_id = id
  ```
- #### Получение списка самых популярных фильмов, c колличеством фильмов count
  ```SQL
  SELECT *
  FROM films f
  JOIN likes l ON f.film_id = l.film_id
  GROUP BY f.film_id
  ORDER BY SUM(l.user_id) DESC
  LIMIT count
  ```

- #### Получение всех пользователей.
  ```SQL
  SELECT *
  FROM users
  ```

- #### Получение пользователя по id.
  ```SQL
