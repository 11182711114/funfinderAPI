# --- !Ups

ALTER TABLE Message
ADD COLUMN seen boolean DEFAULT false;

CREATE TABLE user_match_seen (
	event int NOT NULL,
	event_seen int NOT NULL,
	FOREIGN KEY (event) REFERENCES Event(eventId) ON DELETE cascade,
	FOREIGN KEY (event_seen) REFERENCES Event(eventId),
	PRIMARY KEY (event, event_seen)
);

ALTER TABLE Profile
ADD COLUMN job varchar DEFAULT plebian;

# --- !Downs

ALTER TABLE Message
DROP COLUMN seen;

DROP TABLE user_match_seen;

ALTER TABLE Profile
DROP COLUMN job;