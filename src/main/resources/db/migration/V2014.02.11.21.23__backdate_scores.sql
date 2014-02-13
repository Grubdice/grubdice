update game_results r
  set score =
    (select count(*) from game_results where game_id = r.game_id and place_in_game > r.place_in_game) - (select count(*) from game_results where game_id = r.game_id and place_in_game < r.place_in_game)
  where score is null;