DROP DATABASE IF EXISTS gpconnect1_2_8;
CREATE DATABASE gpconnect1_2_8 DEFAULT CHARACTER SET utf8;

/* Delete the user (grant all to workaround MySQL not supporting 'IF EXISTS' for users) */
GRANT ALL ON gpconnect1_2_8.* TO 'gpconnectdbuser' IDENTIFIED BY 'gpc0nn3ct';
DROP USER 'gpconnectdbuser';
FLUSH PRIVILEGES;

/* Create a new user with full privileges */
CREATE USER 'gpconnectdbuser' IDENTIFIED BY 'gpc0nn3ct';
GRANT ALL ON gpconnect1_2_8.* TO 'gpconnectdbuser'@'%' IDENTIFIED BY 'gpc0nn3ct';
GRANT ALL ON gpconnect1_2_8.* TO 'gpconnectdbuser'@'localhost' IDENTIFIED BY 'gpc0nn3ct';
FLUSH PRIVILEGES;
