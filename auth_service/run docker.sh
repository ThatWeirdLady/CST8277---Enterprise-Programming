docker run \
--name twit \
-e POSTGRES_USER=root \
-e POSTGRES_DB=twit \
-e POSTGRES_PASSWORD=pass \
-p 5432:5432 \
-d \
postgres