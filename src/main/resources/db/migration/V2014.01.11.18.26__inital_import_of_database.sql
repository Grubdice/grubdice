--
-- PostgreSQL database dump
--

-- SET statement_timeout = 0;
SET lock_timeout = 0;
-- SET client_encoding = 'UTF8';
-- SET standard_conforming_strings = on;
-- SET check_function_bodies = false;
-- SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

-- CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

-- COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

--
-- SET search_path = public, pg_catalog;
--
-- SET default_tablespace = '';
--
-- SET default_with_oids = false;

--
-- Name: bids; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE bids (
    id integer NOT NULL PRIMARY KEY,
    facevalue integer NOT NULL,
    quantity integer NOT NULL,
    player_id integer,
    turn_id integer
);


--ALTER TABLE public.bids OWNER TO grubdice;

--
-- Name: game_results; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE game_results (
    id integer NOT NULL PRIMARY KEY,
    score integer,
    game_id integer,
    player_id integer
);


--ALTER TABLE public.game_results OWNER TO grubdice;

--
-- Name: games; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE games (
    id integer NOT NULL PRIMARY KEY,
    note varchar(255),
    players integer,
    start_time timestamp,
    start_timezone varchar(255),
    type varchar(255)
);


--ALTER TABLE public.games OWNER TO grubdice;

--
-- Name: games_turns; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE games_turns (
    games_id integer NOT NULL,
    turns_id integer NOT NULL
);


--ALTER TABLE public.games_turns OWNER TO grubdice;

--
-- Name: nick_names; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE nick_names (
    id integer NOT NULL PRIMARY KEY,
    nickname varchar(255),
    player_id integer
);


--ALTER TABLE public.nick_names OWNER TO grubdice;

--
-- Name: players; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE players (
    id integer NOT NULL PRIMARY KEY,
    name varchar(255)
);


--ALTER TABLE public.players OWNER TO grubdice;

--
-- Name: players_nick_names; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE players_nick_names (
    players_id integer NOT NULL,
    nicknames_id integer NOT NULL
);


--ALTER TABLE public.players_nick_names OWNER TO grubdice;

--
-- Name: turns; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE turns (
    id integer NOT NULL PRIMARY KEY,
    turnnumber integer NOT NULL,
    game_id integer
);


--ALTER TABLE public.turns OWNER TO grubdice;

--
-- Name: turns_bids; Type: TABLE; Schema: public; Owner: grubdice; Tablespace: 
--

CREATE TABLE turns_bids (
    turns_id integer NOT NULL,
    bids_id integer NOT NULL
);

--
-- Name: uk_7okrpledpbtdyrj7yjt9c9tad; Type: CONSTRAINT; Schema: public; Owner: grubdice; Tablespace: 
--

ALTER TABLE turns_bids
    ADD CONSTRAINT uk_7okrpledpbtdyrj7yjt9c9tad UNIQUE (bids_id);


--
-- Name: uk_bl2s1yadu0xpfl86asm57nff5; Type: CONSTRAINT; Schema: public; Owner: grubdice; Tablespace: 
--

ALTER TABLE players_nick_names
    ADD CONSTRAINT uk_bl2s1yadu0xpfl86asm57nff5 UNIQUE (nicknames_id);


--
-- Name: uk_mecitn3gboxa5ldso9gyjf5cj; Type: CONSTRAINT; Schema: public; Owner: grubdice; Tablespace: 
--

ALTER TABLE games_turns
    ADD CONSTRAINT uk_mecitn3gboxa5ldso9gyjf5cj UNIQUE (turns_id);


--
-- Name: uk_pblmuavgrnr991e41662asko; Type: CONSTRAINT; Schema: public; Owner: grubdice; Tablespace: 
--

ALTER TABLE players
    ADD CONSTRAINT uk_pblmuavgrnr991e41662asko UNIQUE (name);


--
-- Name: fk_1kabyhak4yqu0kuvnhcwpjkn4; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE turns_bids
    ADD CONSTRAINT fk_1kabyhak4yqu0kuvnhcwpjkn4 FOREIGN KEY (turns_id) REFERENCES turns(id);


--
-- Name: fk_283vfsimtwqvghxuwcdtc8wo; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE bids
    ADD CONSTRAINT fk_283vfsimtwqvghxuwcdtc8wo FOREIGN KEY (turn_id) REFERENCES turns(id);


--
-- Name: fk_4mwyjpo1rjm5o0uv7dbgj6kwu; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE turns
    ADD CONSTRAINT fk_4mwyjpo1rjm5o0uv7dbgj6kwu FOREIGN KEY (game_id) REFERENCES games(id);


--
-- Name: fk_7ddvvhe8div3x740a64r728kr; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE game_results
    ADD CONSTRAINT fk_7ddvvhe8div3x740a64r728kr FOREIGN KEY (game_id) REFERENCES games(id);


--
-- Name: fk_7flw7sbopj0dv1av77sajp82p; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE games_turns
    ADD CONSTRAINT fk_7flw7sbopj0dv1av77sajp82p FOREIGN KEY (games_id) REFERENCES games(id);


--
-- Name: fk_7okrpledpbtdyrj7yjt9c9tad; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE turns_bids
    ADD CONSTRAINT fk_7okrpledpbtdyrj7yjt9c9tad FOREIGN KEY (bids_id) REFERENCES bids(id);


--
-- Name: fk_bl2s1yadu0xpfl86asm57nff5; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE players_nick_names
    ADD CONSTRAINT fk_bl2s1yadu0xpfl86asm57nff5 FOREIGN KEY (nicknames_id) REFERENCES nick_names(id);


--
-- Name: fk_bwsjmb9n701gx9alcy19spy1s; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE players_nick_names
    ADD CONSTRAINT fk_bwsjmb9n701gx9alcy19spy1s FOREIGN KEY (players_id) REFERENCES players(id);


--
-- Name: fk_e311cs6ph6efibipbkof9jh7r; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE game_results
    ADD CONSTRAINT fk_e311cs6ph6efibipbkof9jh7r FOREIGN KEY (player_id) REFERENCES players(id);


--
-- Name: fk_egjj81ghlnt4xonlixjbt9wbg; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE bids
    ADD CONSTRAINT fk_egjj81ghlnt4xonlixjbt9wbg FOREIGN KEY (player_id) REFERENCES players(id);


--
-- Name: fk_fu6g2g6wmcugnvt0hmtnicxw4; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE nick_names
    ADD CONSTRAINT fk_fu6g2g6wmcugnvt0hmtnicxw4 FOREIGN KEY (player_id) REFERENCES players(id);


--
-- Name: fk_mecitn3gboxa5ldso9gyjf5cj; Type: FK CONSTRAINT; Schema: public; Owner: grubdice
--

ALTER TABLE games_turns
    ADD CONSTRAINT fk_mecitn3gboxa5ldso9gyjf5cj FOREIGN KEY (turns_id) REFERENCES turns(id);


--
-- PostgreSQL database dump complete
--

