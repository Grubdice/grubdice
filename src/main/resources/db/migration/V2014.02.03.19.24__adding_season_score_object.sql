CREATE SEQUENCE season_score_id_seq START 1;

create table season_scores
  (id   INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('season_score_id_seq'),
   player_id INTEGER references players,
   season_id INTEGER references seasons,
   current_score INTEGER);


insert into season_scores (player_id, current_score, season_id)
  select players.id, players.current_score, (select id from seasons limit 1) from players players;