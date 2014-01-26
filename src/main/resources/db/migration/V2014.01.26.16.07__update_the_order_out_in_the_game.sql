update game_results gr
  set place_in_game =
    ( select count(*) from game_results where score > gr.score and game_id = gr.game_id )
  where
    score is not null
    and place_in_game is null;