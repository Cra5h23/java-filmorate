CREATE TABLE IF NOT EXISTS ratings(
rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films(
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating_id INTEGER REFERENCES ratings (rating_id),
name VARCHAR NOT NULL,
description VARCHAR(200),
release_date DATE CHECK release_date > '1895-12-28' NOT NULL,
duration INTEGER CHECK duration > 0
);

CREATE TABLE IF NOT EXISTS genres(
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
genre_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres(
film_id INTEGER REFERENCES films (film_id),
genre_id INTEGER REFERENCES genres (genre_id),
PRIMARY KEY(film_id,genre_id)
);

CREATE TABLE IF NOT EXISTS users(
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR NOT NULL UNIQUE,
login VARCHAR NOT NULL UNIQUE,
name VARCHAR,
birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS likes(
film_id INTEGER REFERENCES films (film_id),
user_id INTEGER REFERENCES users (user_id),
PRIMARY KEY(film_id,user_id)
);

CREATE TABLE IF NOT EXISTS friends(
user_id INTEGER REFERENCES users (user_id) ,
friend_id INTEGER REFERENCES users (user_id),
status boolean DEFAULT FALSE NOT NULL
);

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
islike BOOLEAN NOT NULL,
PRIMARY KEY(review_id,user_id)
);

