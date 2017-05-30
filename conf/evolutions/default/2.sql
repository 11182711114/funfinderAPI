# --- !Ups

ALTER TABLE Message
ADD COLUMN seen boolean;

CREATE TABLE user_match_seen (
	event int NOT NULL,
	event_seen int NOT NULL,
	FOREIGN KEY (event) REFERENCES Event(eventId) ON DELETE cascade,
	FOREIGN KEY (event_seen) REFERENCES Event(eventId),
	PRIMARY KEY (event, event_seen)
)

# --- !Downs

ALTER TABLE Message
DROP COLUMN seen;

DROP TABLE user_match_seen;