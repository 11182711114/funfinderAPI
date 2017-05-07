# Users schema

# --- !Ups

CREATE TABLE User (
    id int(11) NOT NULL AUTO_INCREMENT,
    firstName varchar(45) NOT NULL,
    lastName varchar(45) NOT NULL,
    age int(11) NOT NULL,
    email varchar(45) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Location (
	adress varchar(45) NOT NULL,
	latitude varchar(45) NOT NULL,
	longitude varchar(45) NOT NULL,
	id int(11) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE UserLocation (
	userId int(11) NOT NULL,
	location int(11),
	FOREIGN KEY (userId) REFERENCES User(id) ON DELETE cascade,
	FOREIGN KEY (location) REFERENCES Location(id),
	PRIMARY KEY (userId, location)
);

CREATE TABLE RestaurantLocation (
	id int(11) NOT NULL,
	location int(11) NOT NULL,
	FOREIGN KEY (location) REFERENCES Location(id) ON DELETE cascade,
	PRIMARY KEY (id)
);

CREATE TABLE UserSettings (
	userPrefs varchar(45),
	user int(11) NOT NULL,
	pushnotiser boolean NOT NULL,
	FOREIGN KEY (user) REFERENCES User(id) ON DELETE cascade,
	PRIMARY KEY (user)
);

CREATE TABLE Restaurant (
	id int(11) NOT NULL,
	name varchar(45) NOT NULL,
	rating int(11),
	PRIMARY KEY (id)	
);

# --- !Downs

DROP TABLE User;

DROP TABLE Restaurant;

DROP TABLE Location;

DROP TABLE UserLocation;

DROP TABLE RestaurantLocation;

DROP TABLE UserSettings;
