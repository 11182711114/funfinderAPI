# --- !Ups

ALTER TABLE Message 
ADD COLUMN seen boolean DEFAULT false;

# --- !Downs

ALTER TABLE Message 
DROP COLUMN seen;	