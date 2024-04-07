MERGE INTO genres(genre_id ,genre_name)
VALUES (1, 'Комедия'), (2,'Драма'), (3,'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

MERGE INTO ratings(rating_id, rating_name)
VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

--Данные для тестов удаления
--MERGE INTO genres(genre_id ,genre_name)
--VALUES (7, 'Комедия, Adf');
--
--MERGE INTO films(film_id, rating_id, name, description, release_date, duration)
--VALUES (1, 2, 'testFilm1', 'testDescription1', '1998-01-05', 120);
--
--MERGE INTO users(user_id, email, login, name, birthday)
--VALUES (1, 'testEmail1@test.com', 'testLogin', 'testName', '1991-10-05');
--
--MERGE INTO directors(director_id, director_name)
--VALUES (1, 'testDirector');
--
--MERGE INTO film_genres(film_id, genre_id)
--VALUES (1, 1), (1,2), (1,3), (1,7);
--
--MERGE INTO films_directors(film_id, director_id)
--VALUES (1, 1);
--
--MERGE INTO likes(film_id, user_id)
--VALUES (1, 1)

