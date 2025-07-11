DROP TABLE IF EXISTS PLAYERS;

-- Create a table from the csv
CREATE TABLE PLAYERS AS SELECT * FROM CSVREAD('Player.csv');

-- Index for PLAYERID (primary key, usually indexed by default)
CREATE INDEX IF NOT EXISTS idx_player_id ON PLAYERS (PLAYERID);

-- Index for BIRTHCOUNTRY (for faster queries by country)
CREATE INDEX IF NOT EXISTS idx_birth_country ON PLAYERS (BIRTHCOUNTRY);