# Users schema

# --- !Ups

CREATE TABLE Location (
	adress varchar(45) NOT NULL,
	latitude varchar(45) NOT NULL,
	longitude varchar(45) NOT NULL,
	id int(11) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE User (
    id int(11) NOT NULL AUTO_INCREMENT,
    firstName varchar(45) NOT NULL,
    lastName varchar(45) NOT NULL,
    birthdate date NOT NULL,
    email varchar(45) NOT NULL,
    password varchar(255) NOT NULL,
    loc_id int(11),
    created TIMESTAMP DEFAULT '0000-00-00 00:00:00',
	updated TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (loc_id) REFERENCES Location(id) ON DELETE cascade,
    PRIMARY KEY (id),
    CONSTRAINT emailAK UNIQUE (email)
);

CREATE TRIGGER insertCreatedTrigger BEFORE INSERT ON User 
FOR EACH ROW 
SET NEW.created = now();

CREATE TABLE Message (
	id int NOT NULL,
	message varchar(500) NOT NULL,
	`from` int(11) NOT NULL,
	`to` int(11) NOT NULL,
	sent TIMESTAMP DEFAULT now(),
	FOREIGN KEY (`from`) REFERENCES User(id) ON DELETE cascade,
	FOREIGN KEY (`to`) REFERENCES User(id) ON DELETE cascade,
	PRIMARY KEY (id)
);

CREATE TABLE UserSettings (
	id int(11) NOT NULL AUTO_INCREMENT,
	user_prefs varchar(45),
	user int(11) NOT NULL,
	push_notices boolean NOT NULL,
	FOREIGN KEY (user) REFERENCES User(id) ON DELETE cascade,
	PRIMARY KEY (id)
);

CREATE TABLE Restaurant (
	id int(11) NOT NULL,
	name varchar(45) NOT NULL,
	rating int(11),
	PRIMARY KEY (id)	
);

# --- !Downs

DROP TABLE Message;

DROP TABLE UserSettings;

DROP TABLE User;

DROP TABLE Restaurant;

DROP TABLE Location;

