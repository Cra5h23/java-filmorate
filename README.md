# Java-Filmorate

Java-Filmorate проект приложения соцсети где вы можете добавлять друг друга в друзья, искать фильмы по названию фильма
и его режжисёру, оставлять отзывы, ставить лайки, находить общие с другом фильмы.

## Содержание:

1. Описание основных функций.
    * Функциональность пользователи.
    * Функциональность фильмы.
    * Функциональность отзывы.
    * Функциональность друзья.
    * Функциональность лайки.
    * Функциональность Поиск.
    * Функциональность рекомендации.
    * Функциональность лента событий.
    * Функциональность общие фильмы.

2. Структура Базы данных.
    * Схема базы данных.
    * Краткое описание всех таблиц.
    * Примеры запросов.
   
3. Состав команды.

## Описание основных функций.

### Приложение Filmorate имеет следующии функции.

* #### Функциональность пользователи.
    * Вы можете добавить нового пользователя введя данные такие как: логин, день рождения,
      электронную почту и имя пользователя.
    * Обновить данные пользователя.
    * Вы можете запросить список всех пользователей.
    * Запросить пользователя по его id.
    * Удалить пользователя.

* #### Функциональность фильмы.
    * Вы можете добавить новый фильм введя такие данные как: название фильма, описание фильма, жанр
      фильма, год выпуска фильма, продолжительность фильма, его жанры, возрастной рейтинг и его режжисёры.
    * Обновить данные фильма.
    * Запросить список всех фильмов.
    * Запросить фильм по его id.
    * Удалить фильм.

* #### Функциональность отзывы.
    * Пользователи могут оставлять отзывы к фильмам.
    * Пользователи могут ставить рейтинг отзывам.
    * Можно запросить список всех отзывов для фильма.

* #### Функциональность друзья.
    * Пользователи могут добавлять друг друга в друзья.
    * Пользователи могут подтверждать стаус дружбы.
    * Пользователи могут запрашивать список друзей.
    * Пользователи могут запрашивать список общих друзей с другим пользователем.
    * Пользователи могут удалять из друзей друг друга.

* #### Функциональность лайки.
    * Пользователи могут ставить лайки фильмам.
    * Пользователи могут удалять лайки у фильмов.

* #### Функциональность поиск.
    * Можно выполнить поиск самых популярных фильмов.
    * Можно выполнить поиск по названию фильма, а также его режжисёру.

* #### Функциональность рекомендации.
    * Вывод списка рекомендованных пользователю фильмов.

* #### Функциональность лента событий.
    * Просмотр последних событий на платформе для пользователя таких как: добавление в друзья, удаление из друзей, лайки
      и отзывы, которые оставили друзья пользователя.
* #### Функциональность общие фильмы.
    * Вывод общих с другом фильмов.

## Стуруктура базы данных для приложения Filmorate.

### Схема базы данных.

![Sprint12.drawio.png](src%2Fmain%2Fresources%2FSprint12.drawio.png)

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
    * Первичный ключ составной состоит из полей film_id и user_id.

- #### Таблица film_ganre используется для хранения жанров которым принадлежит фильм.
    * Поле film_id идентификационный номер фильма.
    * Поле genre_id идентификационный номер жанра.
    * Первичный ключ составной состоит из полей film_id и genre_id.

- ##### Таблица genres используется для хранения названий жанров.
    * Поле genre_id идентификационный номер жанра.
    * Поле genre_name название жанра.

- ##### Таблица ratings используется для хранения названий рейтингов.
    * Поле rating_id первичный ключ таблицы, содержит идентификационный номер рейтинга.
    * Поле rating_name содержит название рейтинга.

- ##### Таблица friends используется для хранения друзей пользователей.
    * Поле user_id идентификационный номер пользователя.
    * Поле friend_id идентификационный номер друга.
    * Поле status хранит статус запроса дружбы.

- #### Таблица directors используется для хранения всех режиссёров.
    * Поле director_id первичный ключ таблицы, содержит идентификационный номер режжисёра.
    * Поле director_name имя режиссёра.

- ##### Таблица films_directors используется для хранения режиссёров для фильмов.
    * Поле film_id хранит идентификационный номер фильма.
    * Поле director_id хранит идентификационный номер режиссёра.
    * Первичный ключ составной состоит из полей film_id и director_id.

- ##### Таблица events используется для хранения событий для пользователей.
    * Поле event_id первичный ключ таблицы, содержит идентификационный номер события.
    * Поле time_stamp используется для храниния времени когда произошло событие.
    * Поле user_id внешний ключ используется для связи с таблицей users.
    * Поле event_type используется для хранения типа события (добавление в друзья, установка лайка, отзыв).
    * Поле operation используется для хранения типа операции над событием (добавление, удаление, обновление).
    * Поле entity_id используется для хранения идентификатора сущьности с которой произошло событие.

- ##### Таблица reviews используется для хранения отзывов пользователей к фильмам.
    * Поле review_id первичный ключ таблицы, содержит идентификационный номер отзыва.
    * Поле film_id внешний ключ таблицы используется для связи с таблицей films.
    * Поле user_id внешний ключ таблицы используется для связи с таблицей users.
    * Поле content используется для хранения текста отзыва.
    * Поле ispositive хранит тип отзыва (положительный, негативный).

- ##### Таблица reviewsratings используется для хранения лайков к отзывам
    * Поле review_id внешний ключ таблицы используется для связи с таблицей reviews.
    * Поле user_id внешний ключ таблицы используется для связи с таблицей users.
    * Поле rating используется для хранения оценки отзыва (+1 - лайк/ -1 - дизлайк).

### Примеры запросов.

- #### Получение всех фильмов.
- ```SQL
  SELECT f.*,
  r.RATING_NAME,
  ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,
  ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors
  FROM FILMS f
  LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID
  LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID
  LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID
  LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID
  LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID
  GROUP BY f.FILM_ID
  ```

- #### Получение фильма по id.
  ```SQL
  SELECT f.*,
  r.RATING_NAME,
  ARRAY_AGG(DISTINCT g.GENRE_ID || ';' || g.genre_name) genres,
  ARRAY_AGG(DISTINCT d.DIRECTOR_ID || ';' || d.DIRECTOR_NAME) directors
  FROM FILMS f
  LEFT JOIN RATINGS r ON f.RATING_ID = r.RATING_ID
  LEFT JOIN FILM_GENRES fg ON f.FILM_ID = fg.FILM_ID
  LEFT JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID
  LEFT JOIN FILMS_DIRECTORS fd ON fd.FILM_ID  = f.FILM_ID
  LEFT JOIN DIRECTORS d ON d.DIRECTOR_ID = fd.DIRECTOR_ID
  WHERE f.FILM_ID = :film_id
  GROUP BY f.FILM_ID
  ```

- #### Получение всех пользователей.
  ```SQL
  "SELECT * 
  FROM users"
  ```

- #### Получение пользователя по id.
  ```SQL
  SELECT * 
  FROM users 
  WHERE user_id=?
  ```
## Состав команды
  ### [Николай Радзивон](https://github.com/Cra5h23)
  ### [Зимичева Екатерина](https://github.com/katrinzimi)
  ### [Рахманов Айрат](https://github.com/raaCodeCat)
  ### [Алпеев Кирилл](https://github.com/byLucz)
  ### [Воробьев Егор](https://github.com/Egor151)