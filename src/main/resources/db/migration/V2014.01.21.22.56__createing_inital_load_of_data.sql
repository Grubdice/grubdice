insert into games (note, players, start_time, start_timezone, type) values
	('1', -1, '2014-01-17 03:09:32.357', 'Etc/GMT', 'LEAGUE'),
	('95', 5, '2014-01-17 18:34:25.101', 'Etc/GMT', 'LEAGUE'),
	('102', 6, '2014-01-17 19:01:18.646', 'Etc/GMT', 'LEAGUE'),
	('112', 6, '2014-01-18 00:22:44.114', 'Etc/GMT', 'LEAGUE'),
	('119', 5, '2014-01-18 00:51:41.049', 'Etc/GMT', 'LEAGUE'),
	('127', 4, '2014-01-18 04:28:18.374', 'Etc/GMT', 'LEAGUE'),
	('134', 6, '2014-01-19 19:17:07.713', 'Etc/GMT', 'LEAGUE'),
	('141', 6, '2014-01-21 18:47:26.988', 'Etc/GMT', 'LEAGUE'),
	('148', 6, '2014-01-21 18:50:27.414', 'Etc/GMT', 'LEAGUE'),
	('155', 3, '2014-01-21 18:58:57.535', 'Etc/GMT', 'LEAGUE'),
	('159', 2, '2014-01-21 19:00:00.458', 'Etc/GMT', 'LEAGUE');

insert into players (player_name) values
	('Slotnick'),
	('Birch'),
	('Mercer'),
	('Slayer'),
	('Lee'),
	('Alex'),
	('Lam'),
	('Ethan'),
	('Kuzma'),
	('Brian'),
	('Kristie'),
	('Lev'),
	('James'),
	('Darrel'),
	('Rockford'),
	('JTrebs'),
	('Raquel'),
	('Jake'),
	('Patrick'),
	('Ali'),
	('Kerry'),
	('Audrey'),
	('Guest');

insert into game_results (score, game_id, player_id) values
  (0, (select id from games where note = '1'), (select id from players where player_name = 'Slotnick')),
  (33, (select id from games where note = '1'), (select id from players where player_name = 'Birch')),
  (10, (select id from games where note = '1'), (select id from players where player_name = 'Alex')),
  (17, (select id from games where note = '1'), (select id from players where player_name = 'Mercer')),
  (-6, (select id from games where note = '1'), (select id from players where player_name = 'Brian')),
  (-5, (select id from games where note = '1'), (select id from players where player_name = 'Kristie')),
  (1, (select id from games where note = '1'), (select id from players where player_name = 'Lev')),
  (-3, (select id from games where note = '1'), (select id from players where player_name = 'Slotnick')),
  (-1, (select id from games where note = '1'), (select id from players where player_name = 'James')),
  (5, (select id from games where note = '1'), (select id from players where player_name = 'Darrel')),
  (-1, (select id from games where note = '1'), (select id from players where player_name = 'Rockford')),
  (14, (select id from games where note = '1'), (select id from players where player_name = 'Slayer')),
  (12, (select id from games where note = '1'), (select id from players where player_name = 'Lee')),
  (6, (select id from games where note = '1'), (select id from players where player_name = 'Ethan')),
  (-9, (select id from games where note = '1'), (select id from players where player_name = 'Kuzma')),
  (2, (select id from games where note = '1'), (select id from players where player_name = 'Lam')),
  (4, (select id from games where note = '95'), (select id from players where player_name = 'Slotnick')),
  (2, (select id from games where note = '95'), (select id from players where player_name = 'Lam')),
  (0, (select id from games where note = '95'), (select id from players where player_name = 'Brian')),
  (-2, (select id from games where note = '95'), (select id from players where player_name = 'Lee')),
  (-4, (select id from games where note = '95'), (select id from players where player_name = 'Slayer')),
  (5, (select id from games where note = '102'), (select id from players where player_name = 'Lee')),
  (3, (select id from games where note = '102'), (select id from players where player_name = 'Slotnick')),
  (1, (select id from games where note = '102'), (select id from players where player_name = 'Lam')),
  (-1, (select id from games where note = '102'), (select id from players where player_name = 'Brian')),
  (-3, (select id from games where note = '102'), (select id from players where player_name = 'Slayer')),
  (-5, (select id from games where note = '102'), (select id from players where player_name = 'JTrebs')),
  (5, (select id from games where note = '112'), (select id from players where player_name = 'Jake')),
  (3, (select id from games where note = '112'), (select id from players where player_name = 'Kuzma')),
  (1, (select id from games where note = '112'), (select id from players where player_name = 'Raquel')),
  (-1, (select id from games where note = '112'), (select id from players where player_name = 'Patrick')),
  (-3, (select id from games where note = '112'), (select id from players where player_name = 'Slotnick')),
  (-5, (select id from games where note = '112'), (select id from players where player_name = 'Slayer')),
  (4, (select id from games where note = '119'), (select id from players where player_name = 'Kuzma')),
  (2, (select id from games where note = '119'), (select id from players where player_name = 'Raquel')),
  (0, (select id from games where note = '119'), (select id from players where player_name = 'Slayer')),
  (-2, (select id from games where note = '119'), (select id from players where player_name = 'Patrick')),
  (-4, (select id from games where note = '119'), (select id from players where player_name = 'Slotnick')),
  (3, (select id from games where note = '127'), (select id from players where player_name = 'Ethan')),
  (1, (select id from games where note = '127'), (select id from players where player_name = 'Ali')),
  (-1, (select id from games where note = '127'), (select id from players where player_name = 'Kerry')),
  (-3, (select id from games where note = '127'), (select id from players where player_name = 'Lee')),
  (5, (select id from games where note = '134'), (select id from players where player_name = 'Lam')),
  (3, (select id from games where note = '134'), (select id from players where player_name = 'Mercer')),
  (1, (select id from games where note = '134'), (select id from players where player_name = 'Ethan')),
  (-1, (select id from games where note = '134'), (select id from players where player_name = 'Kerry')),
  (-3, (select id from games where note = '134'), (select id from players where player_name = 'Birch')),
  (-5, (select id from games where note = '134'), (select id from players where player_name = 'Guest')),
  (5, (select id from games where note = '141'), (select id from players where player_name = 'Mercer')),
  (2, (select id from games where note = '141'), (select id from players where player_name = 'Lee')),
  (2, (select id from games where note = '141'), (select id from players where player_name = 'Birch')),
  (-1, (select id from games where note = '141'), (select id from players where player_name = 'Lam')),
  (-3, (select id from games where note = '141'), (select id from players where player_name = 'Ethan')),
  (-5, (select id from games where note = '141'), (select id from players where player_name = 'Audrey')),
  (5, (select id from games where note = '148'), (select id from players where player_name = 'Birch')),
  (3, (select id from games where note = '148'), (select id from players where player_name = 'Lam')),
  (1, (select id from games where note = '148'), (select id from players where player_name = 'Slayer')),
  (-1, (select id from games where note = '148'), (select id from players where player_name = 'Brian')),
  (-4, (select id from games where note = '148'), (select id from players where player_name = 'Mercer')),
  (-4, (select id from games where note = '148'), (select id from players where player_name = 'Kristie')),
  (2, (select id from games where note = '155'), (select id from players where player_name = 'Slayer')),
  (0, (select id from games where note = '155'), (select id from players where player_name = 'Guest')),
  (-2, (select id from games where note = '155'), (select id from players where player_name = 'Guest')),
  (1, (select id from games where note = '159'), (select id from players where player_name = 'Slayer')),
  (-1, (select id from games where note = '159'), (select id from players where player_name = 'Guest'));