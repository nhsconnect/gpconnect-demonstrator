GPConnect Demonstrator
=========
  
The following README contains information related to developing and running the GP Connect Demonstrator locally. If you wish to deploy the demonstrator there is a setup guide within the "Documents" folder of this project named "Deployment Document.docx" which will give you a simple guide to getting a basic instance of the GP Connect Demonstrator up and running.

### Requirements

To develop and run the application locally you must have the following installed:  

| Component | Description |
|---|---|
| Git | A revision control system. |
| Java JDK 8 | is a development support environment for Java. It includes a Java Runtime Environment (JRE), an interpreter (java), a compiler (javac) and many other tools needed in Java development. |
| Maven 3  | A tool for building and managing java projects |
| MySQL  | An open source relational database management system |
| NodeJS | An open source, cross-platform runtime environment for developing networking applications in JavaScript. |
| Ruby | An open source programming language |
| Ruby Gems | A package manager for the Ruby programming language, providing a standard format for distributing Ruby programs and libraries. |  

***

### Getting Started

The first task will be to download the code from github. This can be done using the desktop git tool, an IDE which supports git or by downloading the code as a zip file which you can extract onto your development computer.

Once you have downloaded the code the next action will be to install the development tools and dependancies required to build the project.

### Installation of Development Tools and Programming Languages

#### Install Git for windows:

Install official git release from:   
https://git-scm.com/download/win  

Or install github which includes a git release as well as a GUI interface for git management:   
https://desktop.github.com/   

#### Install the JavaScript package manager NodeJS:  
https://nodejs.org/download/

#### Install Java Development Kit 8:  
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

#### Install Maven 3:  
https://maven.apache.org/download.cgi

#### Install Ruby:  
http://rubyinstaller.org/downloads

#### Install Ruby Gems:  
https://rubygems.org/pages/download

#### Environment Variables

Ensure that the system environment variables for Java, Maven, Ruby, and Ruby Gems are set correctly, as described below...

M2_HOME should point to the install directory of your local Maven install folder, e.g.  
```
M2_HOME C:\Maven\apache-maven-3.3.3
```

JAVA_HOME should point to the install directory of your local Java JDK install folder, e.g.  
```
JAVA_HOME C:\Program Files\Java\jdk1.8.0_65
```

RUBY_HOME should point to the install directory of your local Ruby lang install folder, e.g.  
```
RUBY_HOME C:\Ruby22-x64
```

RUBYGEMS_HOME should point to the install directory of your local Ruby Gems install folder, e.g.  
```
RUBYGEMS_HOME C:\Javascript\rubygems-2.5.1
```

PATH should contain the bin directory of both M2_HOME and JAVA_HOME, e.g.  
```
...;%JAVA_HOME%\bin;%M2_HOME%\bin;%RUBY_HOME%\bin;%RUBYGEMS_HOME%\bin;...
```



### Installation of the MySQL Database

Install MySQL:  
http://dev.mysql.com/downloads/installer/

When using the Windows installer above, the PATH variables are automatically set.

Create the database locally by executing the scripts found in the following directory:  
*gpconnect-database/src/main/resources/sql/legacy*

These scripts are to be run in the order specified in the following file (located in the directory described above):  
*sql_script_run_order.info*



### Initialising the Tomcat Context File (XML Descriptor):

Throughout the Java side of the application, environment properties are used. These are properties which correspond to
certain settings, such as:  
* Addresses of data sources  
* Usernames and passwords for data sources  
* Specific endpoints to be used for queries  

To find an example of this file, you can copy the a fully working example of one, which is located in the following
directory:  
*gp-connect-demonstrator-api\src\main\resources\config\tomcat-context-example.xml*

Copy the file to the *root directory* of the project. It is imperative that this context file is located here when on a 
development environment.

Rename the context file to:  
*context-gpconnect.xml*  



### Installing front end packages

Install Grunt, the JavaScript task runner. You may need to be root user:  
```sh
npm install -g grunt-cli bower
```

Install Sass:  
```sh
gem install sass
```

Navigate to the webapp folder of the GPConnect project:  
```sh
cd {projectRoot}\webapp
```

Install all packages used in the GPConnect project. If you are prompted to select a version of AngularJS, select v1.3.12:  
```sh
bower install
```

Update Bower:  
```sh
bower update
```

Update NodeJS:  
```sh
npm update
```
  

### Running the Application

Open up a shell and navigate to the *root directory* of the project.  
```sh
cd {projectRoot}
```

Use the following command to spin up an instance of a development server (the Java API code):    
```sh
mvn clean package -Pwebapp:run
```

If you're experiencing build errors, execute the following commands:   
```sh
java -version
```

```sh
mvn --version 
```

```sh
ruby --version
```

```sh
gem --version 
```

If they do not return a suitable response, ensure that the system environment variables described above are pointing
to the correct install directory, and that the *\bin* directories within them are on your PATH system environment variable.

### Running the front end

Now that the server is running you can start up the front end of the GP Connect demonstrator. You will need to open up a second command shell and use grunt to serve the web assets as follows:

Firstly, change the current directory to the webapp package within the root directory of the project.  
```sh
cd {projectRoot}\webapp
```

Serving the web assets will also watch for changes to the front end code, and re-serve those assets (used to facilitate 
speedy development of the UI).

These assets, and the features and themes enabled, are centred around the idea of a specific tenant. There is currently only the default 
GPConnect tenant but others can be added when required.

In order to run the application using the standard GPConnect tenant:  
```sh
grunt serve
```  


### Deployment and Server Configuration

Following the same logic shown above for serving the web assets, but use the *build* task instead of the *serve* task:  
```sh
grunt build
```  


The build task minifies and uglifies the front end code in the webapp directory of the project, and packages it up 
in the gpconnect-demonstrator-api module under the following directory:  
*gpconnect-demonstrator-api\src\main\webapp* 

Once you have run the grunt build you can compile and package the project using maven which will create the WAR file you can deploy to a application server and it will contain all the latest frontend and backend code.
```sh
mvn clean package
```  

It is important to run the "grunt build" before the "mvn clean package" as this means that the packaged *gpconnect-demonstrator-api* module will contain the built front end along with the back end code.

The built project will appear as a WAR file in the following directory, with the name "gpconnect-demonstrator-api.war":
```sh
{projectRoot}\gpconnect-demonstrator-api\target
```
The WAR file can be deployed to an application server, such as tomcat. For information on creating a simple tomcat deployment for testing purposes take a look at the "Deployment Document.docx" in the following directory:
```sh
{projectRoot}\Documents
```

### Data Clear Down
For Appointments and Tasks there is a clear down process which is scheduled using a Spring Scheduled event configured with a "cron" string. When the clear down task runs it will delete all GP Connect Demonstrator Tasks previously added. It will also delete all Appointments and remove the currently available Slots, after which it will try and build a new set of slots using a slots sample data file.

The "cron" string which controls the scheduled event can be found in the environmental properties files with the name "legacy.datasource.cleardown.cron"
The file which it uses to build slots should be pointed to by the environmental properties with the name "legacy.datasource.refresh.slots.file"

The properties can be found in the tomcat-context-example.xml file and the example slots data file can be found in the root of the project folder, named "slots.txt"
  
  

### A Bit More on Tenants

As mentioned previously, the application is centred around the concept of a tenant. This idea can be thought of as a 
specific instance of the system, and the associated themes and behaviour of that instance.

Using the system under the precept of a tenant will alter the look and feel of the site, and will also activate or deactivate 
functionality.

The current tenants programmed into GPConnect are:  
* gpconnect

Currently the default and only tenant is *gpconnect*, meaning that there is no need to specify the *--tenant* flag on a 
serve or build. However, the mechanism is there so that different tenants, and their associated profile, can be developed. 

In order to make use of a tenant's version of the site, you may either serve or build the site with the *--tenant* argument 
appended to the grunt command, e.g.  
```sh
grunt serve --tenant=gpconnect
```

Or...  
```sh
grunt build --tenant=gpconnect
```

##### ENJOY!
