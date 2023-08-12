# Hiring-Challenge
This repo is created to solve a hiring challenge assignment. (I'll update the challenge name once the challenge is over)

**If you want to understand what is the task then please refer Project Requirement:**
https://github.com/rohityadav-me/Hiring-Challenge/blob/main/ProjectRequirement.md

**If you want to try it then please visit:** https://bitespeedchallenge.onrender.com/

*Note: It is hosted on free tier hosting so it might be slow or might not even work if I have ran out of my free hosting limit.*

---
### Steps for local development setup
#### Setup Relational Database
* First create a database named `fluxkart`
* Create a table named `contact` inside `fluxkart` database using the query given below:
  
  ```sql
  CREATE TABLE Contact (id serial PRIMARY KEY, phone_number text, email text, linked_id INTEGER, link_precedence VARCHAR(10) CHECK (link_precedence IN ('primary', 'secondary')), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, deleted_at TIMESTAMP);
* Now make sure you change the `application.properties` file present in `src/main/resources`
  ```bash
  server.port:8082
  spring.datasource.url=jdbc:postgresql://localhost:5432/fluxkart 
  spring.datasource.username=postgres
  spring.datasource.password=0000
  spring.datasource.driver-class-name=org.postgresql.Driver
  server.error.whitelabel.enabled=false
  ```
  You would have to change the url, username and password depending upon your configuration.

#### Setup Spring boot
*  Run the following command in terminal: `$ ./gradlew build bootRun`

After doing these steps your application would run on localhost:8082 (or the port that you might have mentioned)
  
