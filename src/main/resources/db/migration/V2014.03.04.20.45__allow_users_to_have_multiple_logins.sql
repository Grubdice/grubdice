CREATE SEQUENCE player_authentications_seq START 1;
create table player_authentications (
  id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('player_authentications_seq'),
  player_id integer references players,
  google_id varchar(255),
  email_address varchar(255)
);