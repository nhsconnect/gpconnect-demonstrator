DROP DATABASE IF EXISTS gpconnect;
CREATE DATABASE gpconnect DEFAULT CHARACTER SET utf8;

/* Delete the answer user (grant all to workaround MySQL not supporting 'IF EXISTS' for users) */
GRANT ALL ON gpconnect.* TO 'gpconnectdbuser' IDENTIFIED BY 'answer99q';
DROP USER 'gpconnectdbuser';
FLUSH PRIVILEGES;

/* Create a new answer user with full privileges */
CREATE USER 'gpconnectdbuser' IDENTIFIED BY 'gpc0nn3ct';
GRANT ALL ON gpconnect.* TO 'gpconnectdbuser'@'%' IDENTIFIED BY 'gpc0nn3ct';
GRANT ALL ON gpconnect.* TO 'gpconnectdbuser'@'localhost' IDENTIFIED BY 'gpc0nn3ct';
FLUSH PRIVILEGES;