# Users schema

# --- !Ups

CREATE TABLE Location (
	adress varchar(45) NOT NULL,
	latitude varchar(45) NOT NULL,
	longitude varchar(45) NOT NULL,
	id int(11) NOT NULL AUTO_INCREMENT,
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

CREATE TABLE `Profile` (
	user int NOT NULL,
	bio varchar(1000),
	hobbies varchar(1000),
	FOREIGN KEY (user) REFERENCES User(id) ON DELETE cascade,
	PRIMARY KEY (user)
);

CREATE TABLE Message (
	id int NOT NULL AUTO_INCREMENT,
	message varchar(500) NOT NULL,
	`sender` int(11) NOT NULL,
	`receiver` int(11) NOT NULL,
	sent TIMESTAMP DEFAULT now(),
	FOREIGN KEY (`sender`) REFERENCES User(id) ON DELETE cascade,
	FOREIGN KEY (`receiver`) REFERENCES User(id) ON DELETE cascade,
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
	id varchar(30) NOT NULL,
	name varchar(45) NOT NULL,
	rating double(10,1),	
	loc_id int(11),
	PRIMARY KEY (id),
	FOREIGN KEY (loc_id) REFERENCES Location(id) ON DELETE SET NULL
);

# --- !Downs

DROP TABLE `Profile`;

DROP TABLE Message;

DROP TABLE UserSettings;

DROP TABLE User;

DROP TABLE Restaurant;

DROP TABLE Location;

