SET lock_timeout = 0;

CREATE SEQUENCE player_id_seq START 1;
CREATE TABLE players (
  id   INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('player_id_seq'),
  name VARCHAR(255)
);

CREATE SEQUENCE game_id_seq START 1;
CREATE TABLE games (
  id             INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('game_id_seq'),
  note           VARCHAR(255),
  players        INTEGER,
  start_time     TIMESTAMP,
  start_timezone VARCHAR(255),
  type           VARCHAR(255)
);

create sequence turn_id_seq start 1;
CREATE TABLE turns (
  id         INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('turn_id_seq'),
  turnnumber INTEGER NOT NULL,
  game_id    INTEGER references games
);

create sequence bid_id_seq start 1;
CREATE TABLE bids (
  id        INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('bid_id_seq'),
  facevalue INTEGER NOT NULL,
  quantity  INTEGER NOT NULL,
  player_id INTEGER references players,
  turn_id   INTEGER references turns
);

create sequence game_result_id_seq start 1;
CREATE TABLE game_results (
  id        INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('game_result_id_seq'),
  score     INTEGER,
  game_id   INTEGER references games,
  player_id INTEGER references players
);

create sequence nick_name_id_seq start 1;
CREATE TABLE nick_names (
  id        INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('nick_name_id_seq'),
  nickname  VARCHAR(255),
  player_id INTEGER references players
);
