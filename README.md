#  Guest Book application using [Spring Boot](http://projects.spring.io/spring-boot/)

## Prerequisites

- [Git](https://git-scm.com/downloads)
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/) - Maven Wrapper plugin
- [MySQL database](https://dev.mysql.com/downloads/)

## Getting Started

### Clone
- To get started you can simply clone this repository using git:
- On command prompt ```git clone https://github.com/amitmmane/guestBook.git```

### Build an executable JAR and run the application
- Navigate to root directory of project (where POM.xml is present)
- In command prompt execute command ```mvnw clean install```
- Execute follwing command to run the application ```java -jar guestbooktest-0.0.1-SNAPSHOT.jar```
- Open browser tab and hit the url `http://localhost:8080.`

### Running the application in Eclipse
- Import the project in Eclipse as exisitng maven project
- Build the project using M2 pugin
- Run the Project as Spring Boot Application

### MySQL Configuration
- Use `spring.datasource.username` and `spring.datasource.password` from `Application.properties` or change them to connect to MySQL db 
- Execute the sql script from GuestBookDemo/sql/gbscript.sql
