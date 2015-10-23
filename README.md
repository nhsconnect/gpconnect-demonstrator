Ripple IDCR Demonstrator
=========

### Requirements

To develop and run the application locally you must have the following installed:
* Java JDK 8
* Maven 3
* NodeJS
* MySQL

### Installation

Create the database locally by running the scripts found in ripple-database/src/main/resources/sql/legacy.
These are to be run in the order specified in the following file:  
sql_script_run_order.info

Install the JavaScript package manager NodeJS:  
https://nodejs.org/download/

Install Grunt, the JavaScript task runner. You may need to be root user:  
```sh
npm install -g grunt-cli bower
```

Install all packages used in the Ripple project. If you are prompted to select a version of AngularJS, select v1.3.12:  
```sh
cd webapp && bower install
```

Update Bower:  
```sh
cd webapp && bower update
```

Update NodeJS:  
```sh
cd webapp && npm update
```

Install Java Development Kit 8:  
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Install Maven 3:  
https://maven.apache.org/download.cgi

Ensure that the system environment variables for Java and Maven are set correctly, as described below...

M2_HOME should point to the install directory of your local Maven install folder, e.g.  
```
M2_HOME C:\Maven\apache-maven-3.3.3
```

JAVA_HOME should point to the install directory of your local Java JDK install folder, e.g.  
```
JAVA_HOME C:\Java\jdk1.8.0_65
```

PATH should contain the bin directory of both M2_HOME and JAVA_HOME, e.g.  
```
...;%JAVA_HOME%\bin;%M2_HOME%\bin;
```

Ensure that you have a context file located in the root directory of the project. This file contains configuration
which is vital to the application. You can copy the a fully working example of one, which is located here:  
ripple-demonstrator-api\src\main\resources\config\tomcat-context-example.xml

### Running the Application

Open up a shell and navigate to the project root directory. Use the following command to build and start the development
server for the Java API:  
```sh
mvn clean package -Pwebapp:run
```

If you're experiencing build errors, execute the following commands:   
```sh
java -version
```

```sh
mvn -version 
```

If they do not return a suitable response, ensure that your JAVA_HOME and M2_HOME system environment variables are pointing
to the correct install directory, and that the \bin directories within them are on your PATH system environment variable.

Now that the server is running, open up a second shell and serve the web assets. This will also watch for changes:  
```sh
cd webapp && grunt serve
```

### Deployment and Server Configuration 

For a full tutorial on how to deploy the application, read the following article:  
http://dev.rippleosi.org/knowledgebase/server-installation-and-initial-setup/



##### ENJOY!