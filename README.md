GPConnect Demonstrator
=========

This README contains information related to developing, building and running the GP Connect Demonstrator.

### Requirements

To develop and run the application locally you must have the following installed:

| Component | Description |
|---|---|
| Git | A revision control system. |
| Java JDK 8 | The Java Development Kit, which includes the Java Runtime Environment (JRE). |
| Maven 3  | A popular Java build tool. |
| MySQL  | An open source RDBMS. |
| NodeJS | An open source, cross-platform runtime environment for developing networking applications in JavaScript. |
| Ruby | An open source programming language. |
| Ruby Gems | A Ruby package manager. |

***

### Getting Started

First, download the code from GitHub. This can be done using the desktop git tool, an IDE which supports git or by downloading the code as a zip file which you can then extract.

Next, install the dev tools and dependencies....

### Installation of Development Tools and Dependencies

#### Install Git for Windows:

Install official git release:
https://git-scm.com/download/win

Or install GitHub Desktop which also includes a GUI interface for git management:
https://desktop.github.com/

#### Install NodeJS:
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
M2_HOME C:\Maven\apache-maven-3.3.9
```

JAVA_HOME should point to the install directory of your local Java JDK install folder, e.g.
```
JAVA_HOME C:\Program Files\Java\jdk1.8.0_121
```

RUBY_HOME should point to the install directory of your local Ruby lang install folder, e.g.
```
RUBY_HOME C:\Ruby233-x64
```

RUBYGEMS_HOME should point to the install directory of your local Ruby Gems install folder, e.g.
```
RUBYGEMS_HOME C:\Javascript\rubygems-2.6.10
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
```
{projectRoot}/gpconnect-database/src/main/resources/sql/legacy*
```

These scripts are to be run in the order specified in (located in the directory described above):
```
sql_script_run_order.info
```

### Environment configuration

Some settings are specific to the environment the application is running on e.g. database username/password/location.

The environment specific files can be found in:
```
{projectRoot}/config
```

You are welcome to use these configuration files in situ, however we ask that any changes made are *not* committed back to GitHub, unless you're adding a new property. Instead you may wish to copy this entire directory to another location on your machine and point your application to the copied files (how to do this is covered later).

### Installing front end packages

Install Grunt, the JavaScript task runner (you may need to be root user):
```sh
npm install -g grunt-cli bower
```

Install Sass:
```sh
gem install sass
```

Navigate to the webapp folder of the GPConnect project:
```sh
cd {projectRoot}/webapp
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

Open up a shell and navigate to the *root directory* of the project:
```sh
cd {projectRoot}
```

Build the project:
```sh
mvn clean package
```

Now spin up an instance of the application (\<path_to_config\> is the path to the Environment configuration discussed earlier, and must end with a slash):
```sh
java -jar gpconnect-demonstrator-api\target\gpconnect-demonstrator-api.war --server.port=19191 --config.path=<path_to_config>/
```

If you're experiencing build errors, execute the following commands:
```sh
java -version
mvn --version
ruby --version
gem --version
```

If they do not return a suitable response, ensure that the system environment variables described above are pointing
to the correct install directory, and that the *\bin* directories within them are on your PATH system environment variable.

### Running the front end

Now the server is running you can start up the front end of the GP Connect demonstrator. You will need to open up a second command shell and use grunt to serve the web assets as follows:

Firstly, change the current directory to the webapp package within the root directory of the project.
```sh
cd {projectRoot}\webapp
```

Serving the web assets will also watch for changes to the front end code, and re-serve those assets (to facilitate
speedy UI development).

These assets, and the features and themes enabled, are centred around the idea of a specific tenant. There is currently only the default
GPConnect tenant but others can be added when required.

In order to run the application using the standard GPConnect tenant:
```sh
grunt serve
```

### Deployment and Server Configuration

In a production environment, the GPConnect project will not be running the back end and UI separately. The following method inserts the UI into the main application so it can be run at the same time.

Following the same logic shown above for serving the web assets, but use the *build* task instead of the *serve* task:
```sh
grunt build
```

The *build* task minifies and uglifies the front end code in the webapp directory of the project, and packages it up
in the gpconnect-demonstrator-api module under the following directory:
```
{projectRoot}/gpconnect-demonstrator-api/src/main/webapp
```

Once you've run the build task, rebuild and run the main application (stop any other processes first) using the same command as before. When it starts, you'll now see the UI.

This combined war file can now be run on any environment.

### Data Clear Down
For Appointments and Tasks there is a clear down process which is scheduled using a Spring Scheduled event configured with a "cron" string. When the clear down task runs it will delete all GP Connect Demonstrator Tasks previously added. It will also delete all Appointments and remove the currently available slots, after which it will try and build a new set of slots using a slots sample data file.

The "cron" string which controls the scheduled event can be found in the environmental properties files with the name "legacy.datasource.cleardown.cron".

The file which it uses to build slots should be pointed to by the environmental properties with the name "legacy.datasource.refresh.slots.filename".

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
