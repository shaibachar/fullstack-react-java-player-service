package com.app.playerservicejava;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;

@SpringBootTest
class PlayerServiceJavaApplicationTests {

    @Autowired
    private PlayerService playerService;

    @Test
    void contextLoads() {
    }


    @Test
    void testGetPlayersDefaultPagination() {
        Players players = playerService.getPlayers(null, null);
        assert players != null;
        assert players.getPlayers().size() <= 10;
    }

    @Test
    void testGetPlayersCustomPagination() {
        Players players = playerService.getPlayers(1, 5);
        assert players != null;
        assert players.getPlayers().size() <= 5;
    }


    @Test
    void testGetPlayerByIdValid() {
        String testId = "aasedo01";
        Optional<Player> player = playerService.getPlayerById(testId);
        assert player != null;
        assert player.isPresent();
    }

    @Test
    void testGetPlayerByIdInvalid() {
        String testId = "nonexistent_id";
        Optional<Player> player = playerService.getPlayerById(testId);
        assert player != null;
        assert player.isEmpty();
    }


    @Test
    void testGetPlayersByBirthCountryDefaultPagination() {
        String testCountry = "D.R.";
        Players players = playerService.getPlayersByBirthCountry(testCountry, null, null);
        assert players != null;
        assert players.getPlayers().size() <= 10;
    }

    @Test
    void testGetPlayersByBirthCountryCustomPagination() {
        String testCountry = "D.R.";
        Players players = playerService.getPlayersByBirthCountry(testCountry, 1, 5);
        assert players != null;
        assert players.getPlayers().size() <= 5;
    }

    @Test
    void testGetPlayersByBirthCountryInvalid() {
        String testCountry = "ZZZ";
        Players players = playerService.getPlayersByBirthCountry(testCountry, null, null);
        assert players != null;
        assert players.getPlayers().isEmpty();
    }

}
