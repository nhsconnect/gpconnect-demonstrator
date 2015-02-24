OpenEHR POC
=========

### Installation

To run the application locally you must have the following installed:
* Java RE 8
* Maven 3
* NodeJS
* MySQL

To build and start the developement server for the Java API:
```sh
mvn clean package -Pwebapp:run
```

Install global NPM dependencies:
```sh
npm install -g grunt-cli bower
```

To serve the web assets and watch for changes:
```sh
cd webapp && grunt serve
```
