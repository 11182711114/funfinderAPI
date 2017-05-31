# --- !Ups

CREATE TABLE facebook_login (
	access_token varchar(100) NOT NULL,
	user int(11) NOT NULL,
	FOREIGN KEY (user) REFERENCES User(id),
	PRIMARY KEY(access_token)
);


# --- !Downs

DROP TABLE facebook_login;