create table dog
(
   id integer NOT NULL AUTO_INCREMENT,
   breed varchar(255) not null, 
   imageLink varchar(255) not null,
   primary key(id)
);

create table vote
(
   id integer NOT NULL AUTO_INCREMENT,
   votes integer,
   imageID integer,
   ipAddress varchar(255),
   primary key(id)
);