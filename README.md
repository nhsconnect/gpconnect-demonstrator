IDCR POC
=========

### Requirements

To run the application locally you must have the following installed:
* Java RE 8
* Maven 3
* NodeJS
* MySQL

### Installation

Create the database locally by running the scripts found at idcr-poc/esb/src/main/sql/:
poc_patient_tables.sql
poc_site_table.sql

Install nodejs so we can use its package manager:
https://nodejs.org/download/

Install grunt, the javascript task runner. You may need to be root user:
```sh
npm install -g grunt-cli bower
```

Install all packages:
```sh
cd webapp && bower install
```
If prompted to select a version of angular, select v1.3.12

Update bower:
```sh
cd webapp && bower update
```

Update node:
```sh
cd webapp && npm update
```

Install Java Runtime Environment 8:
http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

Install Maven 3:
https://maven.apache.org/download.cgi

Build and start the developement server for the Java API:
```sh
mvn clean package -Pwebapp:run
```

If you're experiencing build errors:
Check your java is 1.8 or above
```sh
java -version
```

Check maven is using the correct java version:
```sh
mvn -version 
```

In a second shell, run the following command to serve the web assets and watch for changes:
```sh
cd webapp && grunt serve
```




