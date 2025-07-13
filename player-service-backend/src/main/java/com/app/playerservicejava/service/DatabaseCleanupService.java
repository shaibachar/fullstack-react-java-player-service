package com.app.playerservicejava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseCleanupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void cleanPlayersTable() {
        // Remove rows with missing PLAYERID
        jdbcTemplate.update("DELETE FROM PLAYERS WHERE PLAYERID IS NULL");

        // Standardize country names
        jdbcTemplate.update("UPDATE PLAYERS SET BIRTHCOUNTRY = 'USA' WHERE BIRTHCOUNTRY = 'United States'");


        // Impute missing height with average
        jdbcTemplate.update("UPDATE PLAYERS SET HEIGHT = (SELECT AVG(HEIGHT) FROM PLAYERS) WHERE HEIGHT IS NULL");
        // Impute missing weight with average
        jdbcTemplate.update("UPDATE PLAYERS SET WEIGHT = (SELECT AVG(WEIGHT) FROM PLAYERS) WHERE WEIGHT IS NULL");

        // If only birthDay is missing, set to 1
        jdbcTemplate.update("UPDATE PLAYERS SET BIRTHDAY = 1 WHERE BIRTHDAY IS NULL AND BIRTHYEAR IS NOT NULL AND BIRTHMONTH IS NOT NULL");

        // Remove rows missing birthYear or birthMonth
        jdbcTemplate.update("DELETE FROM PLAYERS WHERE BIRTHYEAR IS NULL OR BIRTHMONTH IS NULL");

        // Validate deathYear > birthYear, set deathYear to NULL if invalid
        jdbcTemplate.update("UPDATE PLAYERS SET DEATHYEAR = NULL WHERE DEATHYEAR IS NOT NULL AND BIRTHYEAR IS NOT NULL AND DEATHYEAR <= BIRTHYEAR");

        // Convert debut and finalGame to DATE type if not already (H2 uses CAST, not TRY_CAST)
        jdbcTemplate.update("UPDATE PLAYERS SET DEBUT = CAST(DEBUT AS DATE) WHERE DEBUT IS NOT NULL");
        jdbcTemplate.update("UPDATE PLAYERS SET FINALGAME = CAST(FINALGAME AS DATE) WHERE FINALGAME IS NOT NULL");
    }
}
