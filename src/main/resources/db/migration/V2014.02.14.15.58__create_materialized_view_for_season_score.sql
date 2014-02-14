drop table season_scores;

create materialized view season_scores (id, player_id, season_id, current_score) as
  select nextval('season_score_id_seq'), r.player_id, g.season_id, sum(r.score)
    from game_results r join games g on r.game_id = g.id group by r.player_id, g.season_id with data;

CREATE OR REPLACE FUNCTION fun_refresh_season_score_materialized_view() returns trigger as
$$
begin
  refresh materialized view season_scores;
  return null;
end;
$$
language plpgsql;

DROP TRIGGER IF EXISTS trig_01_refresh_season_score on game_results;

CREATE TRIGGER trig_01_refresh_season_score AFTER TRUNCATE OR INSERT OR UPDATE OR DELETE
ON game_results FOR EACH STATEMENT
EXECUTE PROCEDURE fun_refresh_season_score_materialized_view();
