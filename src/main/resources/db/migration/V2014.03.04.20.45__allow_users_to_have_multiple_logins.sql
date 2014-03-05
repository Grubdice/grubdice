CREATE SEQUENCE player_authentications_seq START 1;
create table player_authentications (
  id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('player_authentications_seq'),
  player_id integer not null references players,
  google_id varchar(255),
  email_address varchar(255)
);

insert into player_authentications(player_id, google_id, email_address)
  select id, google_id, email_address from players where google_id is not null;