# ITI0302 - Robotiklubi Homepage

***

## Description
This project is the backend of the Robotiklubi homepage.

## Information
Information about the project can be found on the wiki: https://gitlab.cs.ttu.ee/alvaht/iti0302-robotiklubi/-/wikis/Home.

## Installation

Running the backend locally:
1. Install Docker ([instructions](https://docs.docker.com/get-docker/)).
2. Create a docker-compose.yml file for the database using [this template](https://pastebin.com/ugVtcrHB). Put a password for the database into the file.
3. Open a terminal in the same folder as the docker-compose.yml file and run the database using `docker-compose up -d`.
4. Clone the backend project onto your machine.
5. Open a terminal in the project folder, run `./mvnw package` and `docker build -t robotiklubi-backend .`.
6. Create a docker-compose.yml file for the backend using [this template](https://pastebin.com/ShPJxvY1).
7. In the same directory create an application.properties file using [this template](https://pastebin.com/PjDw4MgH). Insert your database password and use your local IP address to link to the database.
8. Open a terminal in the same directory, run the backend using `docker-compose up -d`.

Running the backend on a server:
1. Follow server setup instructions detailed [here](https://gitlab.cs.ttu.ee/alvaht/iti0302-robotiklubi-backend/-/wikis/Setting-Up-the-Server).
2. Running the project:\
   A) Tell GitLab Runner to run the project.\
   B) Manually navigate to the folder on the server that contains the docker-compose.yml file corresponding to the database and use the command `docker-compose up -d` to start it. Then navigate to the backend folder and use the same command to start the backend.
## Authors
Mikk Loomets, Sander Pleesi, Alex Vahter, Rainer Viirlaid

## Project status
The project is currently under development.
