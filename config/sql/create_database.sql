/* Create a new poc_legacy database and begin configuration */
DROP DATABASE IF EXISTS gpconnect0;
CREATE DATABASE gpconnect0 DEFAULT CHARACTER SET utf8;

/* Delete the answer user (grant all to workaround MySQL not supporting 'IF EXISTS' for users) */
GRANT ALL ON gpconnect0.* TO 'answer' IDENTIFIED BY 'answer99q';
DROP USER 'answer';
FLUSH PRIVILEGES;

/* Create a new answer user with full privileges */
CREATE USER 'answer' IDENTIFIED BY 'answer99q';
GRANT ALL ON gpconnect0.* TO 'answer'@'%' IDENTIFIED BY 'answer99q';
GRANT ALL ON gpconnect0.* TO 'answer'@'localhost' IDENTIFIED BY 'answer99q';
FLUSH PRIVILEGES;
