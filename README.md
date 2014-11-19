OpenEHR POC
=========


###Building OpenEHR POC

####TODO:
* npm instructions
* ordering of command line snippets

[Once you have your environment setup](https://github.com/AnswerConsulting/open-ehr/wiki) you can package the ESB and run the webapp using:

    mvn clean package -Pwebapp:run

To build the front end only, you can use:

    grunt build
  
To view the 'unminified' front end components (useful when debugging), you can use:

    grunt serve

