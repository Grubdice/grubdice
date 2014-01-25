alter table players add column current_score integer default 0 not null;

update players as p set current_score = (select sum(score) from game_results where player_id = p.id);