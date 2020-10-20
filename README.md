#  Guest Book application using [Spring Boot](http://projects.spring.io/spring-boot/)

## Storyboard and Test report (Junits- Unit and Integartion Test)
- Please find story board of the project here [Storyboard](https://github.com/amitmmane/guestBook/blob/master/GuestBookDemo/documents/gbStoryboard.docx) which has walkthrough of project and screenshots and test reports.

## Prerequisites  

- [Git](https://git-scm.com/downloads)
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/) - Maven Wrapper plugin
- [MySQL](https://dev.mysql.com/downloads/)
- MySQL configurations (Refer @bottom of the page)

## Getting Started

### Clone
- To get started you can simply clone this repository using git
- On command prompt execute ```git clone https://github.com/amitmmane/guestBook.git```

### Build an executable JAR and run the application
- Navigate to root directory of project (where POM.xml is present)
- In command prompt execute command ```mvnw clean install```
- jar will get generated on following path `.\target\GuestBookDemo-0.0.1-SNAPSHOT.jar`
- Execute follwing command to run the application ```java -jar -Ddatasource.username={DB_Username} -Ddatasource.pwd={DB_password} GuestBookDemo-0.0.1-SNAPSHOT.jar```
  .Please replace {DB_Username}  and  {DB_password}  with MySQL database credentials
- Open browser tab and hit the url `http://localhost:8080.`

### Running the application in Eclipse
- Import the project in Eclipse as exisitng maven project
- Build the project using M2 pugin pass the arguments ``-Ddatasource.username={DB_Username}`` and ``-Ddatasource.pwd={DB_password}``
  Please replace {DB_Username}  and  {DB_password} with MySQL database credentials
- Run the Project as Spring Boot Application

### MySQL Configuration
- Use `spring.datasource.username` and `spring.datasource.password` from `Application.properties` or change them as per to connect to MySQL 
- Execute the sql script from [MySQL Script](https://github.com/amitmmane/guestBook/blob/master/GuestBookDemo/documents/gbscript.sql).
