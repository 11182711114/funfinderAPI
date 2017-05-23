# Users schema

# --- !Ups


CREATE TABLE Location (
	adress varchar(160) NOT NULL,
	latitude varchar(45) NOT NULL,
	longitude varchar(45) NOT NULL,
	id int(11) NOT NULL AUTO_INCREMENT,
	CONSTRAINT UNIQUE (adress, latitude, longitude), 
	PRIMARY KEY (id)
);

CREATE TABLE Restaurant (
	id varchar(30) NOT NULL,
	name varchar(45) NOT NULL,
	rating double(10,1),	
	location int(11),
	PRIMARY KEY (id),
	FOREIGN KEY (location) REFERENCES Location(id) ON DELETE SET NULL
);

CREATE TABLE Event (
	eventId int(11) NOT NULL AUTO_INCREMENT,
	date date NOT NULL,
	time time NOT NULL,
	location varchar(160),
	PRIMARY KEY (eventId)
);

CREATE TABLE User (
    id int(11) NOT NULL AUTO_INCREMENT,
    firstName varchar(45) NOT NULL,
    lastName varchar(45) NOT NULL,
    birthdate date NOT NULL,
    loc int(11),
    event int(11),
    email varchar(45) NOT NULL,
    password varchar(255) NOT NULL,
    created TIMESTAMP DEFAULT '0000-00-00 00:00:00',
	updated TIMESTAMP DEFAULT now() ON UPDATE now(),
    FOREIGN KEY (loc) REFERENCES Location(id) ON DELETE SET NULL,
    FOREIGN KEY (event) REFERENCES Event(eventId) ON DELETE SET NULL,
    PRIMARY KEY (id),
    CONSTRAINT emailAK UNIQUE (email)
);

CREATE TABLE Event_Rest (
	atRest varchar(30),
	atEvent int(11),
	FOREIGN KEY(atRest) REFERENCES Restaurant(id) ON DELETE CASCADE,
	FOREIGN KEY(atEvent) REFERENCES Event(eventId) ON DELETE CASCADE
);

CREATE TABLE BookedEvent (
	id int(11) NOT NULL AUTO_INCREMENT,
	user1 int(11),
	user2 int(11),
	restaurant varchar(30),
	date date NOT NULL,
	time time NOT NULL,
	FOREIGN KEY(user1) REFERENCES User(id) ON DELETE CASCADE,
	FOREIGN KEY(user2) REFERENCES User(id) ON DELETE CASCADE,
	FOREIGN KEY(restaurant) REFERENCES Restaurant(id),
	PRIMARY KEY (id)
	);
	

CREATE TRIGGER insertCreatedTrigger BEFORE INSERT ON User 
FOR EACH ROW 
SET NEW.created = now();

CREATE TABLE Profile (
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
	location int(11),
	PRIMARY KEY (id),
	FOREIGN KEY (location) REFERENCES Location(id) ON DELETE SET NULL    
);



# --- !Downs

DROP TABLE Profile;

DROP TABLE Message;

DROP TABLE UserSettings;

DROP TABLE User;

DROP TABLE Restaurant;

DROP TABLE Location;

DROP TABLE Event;

DROP TABLE Event_Rest;

DROP TABLE BookedEvent;