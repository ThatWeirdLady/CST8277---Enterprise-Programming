# Springboot skeleton with docker

This repo is a skeleton for a springboot application that also has some docker setup. Specifically for CST8277

# How to run

1. Download the repo
1. From the root of the repo `mvn spring-boot:run`

# How to build docker

1. `docker build -t IMAGE_TAG:VERSION .`


# How to run in docker

1. `docker run --rm -p 8083:8083 IMAGE_TAG:VERSION`

# How to test

While the application is running, in a different terminal, run the command `curl localhost:8083` and you should see `Hello World!` as a response.
