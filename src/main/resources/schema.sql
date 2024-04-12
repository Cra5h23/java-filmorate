-- Дроп таблиц для новых тестов
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS films_directors;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS reviewratings;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS directors;
--

--Рейтинги
CREATE TABLE IF NOT EXISTS ratings(
rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_name VARCHAR NOT NULL
);

--Фильмы
CREATE TABLE IF NOT EXISTS films(
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_id INTEGER REFERENCES ratings (rating_id),
name VARCHAR NOT NULL,
description VARCHAR(200),
release_date DATE CHECK release_date > '1895-12-28' NOT NULL,
duration INTEGER CHECK duration > 0
);

--Жанры
CREATE TABLE IF NOT EXISTS genres(
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
genre_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres(
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
PRIMARY KEY(film_id,genre_id)
);
--

--Пользователи
CREATE TABLE IF NOT EXISTS users(
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR NOT NULL UNIQUE,
login VARCHAR NOT NULL UNIQUE,
name VARCHAR,
birthday DATE NOT NULL
);
--

--Лайки
CREATE TABLE IF NOT EXISTS likes(
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
PRIMARY KEY(film_id,user_id)
);
--

--Друзья
CREATE TABLE IF NOT EXISTS friends(
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
status boolean DEFAULT FALSE NOT NULL
);
--

--Режиссёры
CREATE TABLE IF NOT EXISTS directors(
director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
director_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films_directors(
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
director_id INTEGER REFERENCES directors (director_id) ON DELETE CASCADE,
PRIMARY KEY(film_id, director_id)
);
--

--Ревью
CREATE TABLE IF NOT EXISTS reviews(
review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
content VARCHAR NOT NULL,
ispositive BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS reviewratings(
review_id INTEGER REFERENCES reviews (review_id) ON DELETE CASCADE,
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
rating INTEGER NOT NULL,
PRIMARY KEY(review_id,user_id)
);
--

CREATE TABLE IF NOT EXISTS events (
event_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
time_stamp TIMESTAMP,
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
event_type CHARACTER VARYING(50),
operation CHARACTER VARYING(50),
entity_id INTEGER
);
--