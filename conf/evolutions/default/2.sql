# --- !Ups

ALTER TABLE Message (
ADD COLUMN seen boolean DEFAULT false;
);

ALTER TABLE Restaurant(
MODIFY id varchar(30) NOT NULL,
MODIFY rating decimal(10,5),
ADD loc_id int(11),
FOREIGN KEY (loc_id) REFERENCES Location(id) ON DELETE SET NULL
);



# --- !Downs

ALTER TABLE Message 
DROP COLUMN seen;	


#ALTER TABLE Restaurant(
#MODIFY id int(11) NOT NULL,
#MODIFY rating int(11),
#DROP loc_id,
);