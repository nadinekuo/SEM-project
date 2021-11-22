# CSE2115 - Project

### Running 
`gradle bootRun`

### Testing
```
gradle test
```

To generate a coverage report:
```
gradle jacocoTestCoverageVerification
```


And
```
gradle jacocoTestReport
```
The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open index.html in your browser to see the report. 

### Static analysis
```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

### Notes
- You should have a local .gitignore file to make sure that any OS-specific and IDE-specific files do not get pushed to the repo (e.g. .idea). These files do not belong in the .gitignore on the repo.
- If you change the name of the repo to something other than template, you should also edit the build.gradle file.
- You can add issue and merge request templates in the .gitlab folder on your repo.


### Configure database

Before starting the server, you might want to configure your database settings.
We worked with Spring Profiles to switch between H2 for development purposes, and PostgreSQL for production purposes.
This can be done by navigating to `application.properties` and set `spring.profiles.active= ` to either `development` for H2 or `production` for PostgreSQL.
These will invoke the H2Config and PostgresConfig files respectively.

For setting up PostgreSQL, copy the contents of the `application-production.template.properties` file into a new file named `application-production.properties`.
Here, a username and password can be added. Also, `spring.jpa.hibernate.ddl-auto=` can be set to either `create-drop` or `validate`,
causing data to be dropped or preserved respectively after server restart.
This file is ignored by git, thus any changes you make to it will not be pushed to remote.
The same can be done for H2 by using the `application-dev.template.properties` file instead.


When using PostgreSQL, all tables will be inserted into a database named 'sportscentre',
thus this database will have to be created beforehand.


In the folder `nl.tudelft.sem.template.config`, our Config files can be found, serving as database loaders.
