# Project Excellence
Project Excellence (or ProjeX) is a project and resource management tool. With 
ProjeX teams can document the current state of their projects and individuals 
can track their skills with the built-in skill management tools.


## Features
- Project-management 
- Track users skills
- Find other users by their skills


## Technical requirements
### Backend
- Apache Maven: [Download](https://maven.apache.org/download.cgi), 
  [Installation Guide](https://maven.apache.org/install.html)
- Java 12: [Download Oracle OpenJDK](https://jdk.java.net/archive/)
- MariaDB instance [Get for Windows](https://mariadb.com/downloads/), 
  Mac: ``brew install mariadb`` ``->`` ``mysql.server start``

### Frontend
- Node.js, [Download](https://nodejs.org/en/)
- Angular ``npm install -g @angular/cli``

### Deployment
- Docker, [Download](https://www.docker.com/)
- Azure/OpenShift


## Build and run applications
Both applications, frontend and backend, can be built with: `mvn clean package`

To accelerate the build process use the flag `-DskipTests` to skip the execution 
of all tests and `-pl app,base,domain,service,persistence` to only build the backend.
You can then generate the required classes for the frontend by switching to the 
`frontend-angular` directory and running `npm run openapi`. This is overall faster.

All in all you would run the following commands:
````shell
$ mvn clean package -DskipTests -pl app,base,domain,service,persistence
$ cd frontend-angular
$ npm run openapi
````

## Start backend
First set the required environment variables. Adjust as necessary.
```
DATASOURCE_URL=jdbc:mariadb://localhost:3306/projex
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=RootPassword
SSL_ENABLED=true
EMAIL_USERNAME=noreply.projex@gmail.com
EMAIL_PASSWORD=**** (request password from Project Manager / Bernhard Kube)
EMAIL_HOST_SMTP=smtp.googlemail.com
EMAIL_HOST_SMTP_PORT=587
PROJECT_STATUS_CHECK_EMAILS=true

```

Start the application:
````shell
$ java --jar app/target/app-1.1.0-SNAPSHOT-application.jar
````

Start with web interface for Swagger UI:
````shell
$ java --jar app/target/app-1.1.0-SNAPSHOT-application.jar --spring.profiles.active=swagger
````
- [Open Swagger UI (HTTP)](http://localhost:8443/app/swagger-ui.html) 
- [Open Swagger UI (HTTPS)](https://localhost:8443/app/swagger-ui.html) 


## Start frontend

Change to sub-module `frontend-angular` and start the application.

```shell
$ cd frontend-angular/
$ npm run "start with external keycloak"
```
- [Open application (HTTP)](http://localhost:4200)
- [Open application (HTTPS)](https://localhost:4200)


## Open application in Azure
- [Open application in Azure](https://projex.westeurope.cloudapp.azure.com)

## Open application in OpenShift
- [Develop environment](https://projex-develop.ocp02.cloud.lhind.app.lufthansa.com)
- [Integration environment](https://projex.lhind-staging.app.lufthansa.com/)
- [Production environment](https://projex.lhind.app.lufthansa.com/)

## Azure Deployment
Follow the Azure Deployment README in springboot-parent/deployment/azure. 
 
## Supported browsers
Please use Firefox or Google Chrome to start the application. Others browsers 
might not support the application.
 
