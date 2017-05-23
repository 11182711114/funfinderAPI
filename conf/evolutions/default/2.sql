# --- !Ups

ALTER TABLE Message
ADD COLUMN seen boolean;

# --- !Downs

ALTER TABLE Message
DROP COLUMN seen;