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

CREATE TABLE UserLocation (
	userId int(11) NOT NULL,
	location int(11),
	PRIMARY KEY (userId, location),
	FOREIGN KEY (userId) REFERENCES (User.id),
	FOREIGN KEY (location) REFERENCES (Location.ID)
);

CREATE TABLE Location (
	adress varchar(45) NOT NULL,
	coordinates varchar(45) NOT NULL,
	locationId int(11) NOT NULL,
	PRIMARY KEY (locationId)
);

CREATE TABLE RestaurantLocation (
	id int(11) NOT NULL,
	location int(11) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (location) REFERENCES (Location.id)
);

CREATE TABLE UserSettings (
	userPrefs varchar(45),
	user int(11) NOT NULL,
	pushnotiser boolean NOT NULL,
	PRIMARY KEY (user),
	FOREIGN KEY (user) REFERENCES (User.id)	
);

CREATE TABLE Restaurant (
	id int(11) NOT NULL,
	name varchar(45) NOT NULL,
	rating int(11)
	PRIMARY KEY (id)	
);

# --- !Downs

DROP TABLE User;

DROP TABLE UserLocation;

DROP TABLE Location;

DROP TABLE RestaurantLocation;

DROP TABLE UserSettings;

DROP TABLE Restaurant;