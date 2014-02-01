CREATE SEQUENCE season_id_seq START 1;
create table seasons (
  id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('season_id_seq'),
  title varchar(255) default "Awesome Season",
  start_date TIMESTAMP not null,
  end_data TIMESTAMP not null
);

insert into seasons (start_date, end_data) values (now() - interval '3 month', now() + interval '3 month');

alter table games add column season_id INTEGER references seasons;

update games set season_id = (select id from seasons limit 1);

alter table games alter column season_id set not null;