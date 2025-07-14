package com.app.playerservicejava;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;

@SpringBootTest
class PlayerServiceJavaApplicationTests {
    @Test
    void testCsvChunkCacheLogic() {
        String csvFileName = "Player.csv";
        com.app.playerservicejava.service.chat.ChatClientService chatClientService = new com.app.playerservicejava.service.chat.ChatClientService();
        java.util.Map<String, java.util.List<String>> csvChunkCache = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.List<String> chunks = csvChunkCache.get(csvFileName);
        if (chunks == null) {
            String csvData = chatClientService.loadCsvByFileName(csvFileName);
            // Default: 10 rows per chunk
            chunks = chatClientService.splitCsvIntoChunks(csvData, 10);
            csvChunkCache.put(csvFileName, chunks);
        }
        assertNotNull(chunks);
        // Each chunk should start with the header
        for (String chunk : chunks) {
            assert chunk.startsWith("playerID,birthYear,birthMonth,birthDay,birthCountry,birthState,birthCity,deathYear,deathMonth,deathDay,deathCountry,deathState,deathCity,nameFirst,nameLast,nameGiven,weight,height,bats,throws,debut,finalGame,retroID,bbrefID\n");
        }
        for (String part : chunks) {
                String[] lines = part.split("\r?\n");
                assertNotNull(lines, "Each chunk should be split into lines");
                assert lines.length > 0 : "Each chunk should contain at least one line";
                assert lines.length <= 11 : "Each chunk should not exceed 11 lines (header + 10 data rows)";
        }
        // First chunk should contain 10 lines (header + 10 data rows)
        String[] lines = chunks.get(0).split("\r?\n");
        assert lines.length == 11;
    }
    void testSplitCsvIntoChunksWithPlayerCsv() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Player.csv");
        assertNotNull(is, "Player.csv should be available in the classpath");
        String csv;
        try {
            csv = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Player.csv", e);
        }
        com.app.playerservicejava.service.chat.ChatClientService chatClientService = new com.app.playerservicejava.service.chat.ChatClientService();
        java.util.List<String> chunks = chatClientService.splitCsvIntoChunks(csv, 10);
        assertNotNull(chunks);
        // Each chunk should start with the header
        for (String chunk : chunks) {
            assert chunk.startsWith("playerID,birthYear,birthMonth,birthDay,birthCountry,birthState,birthCity,deathYear,deathMonth,deathDay,deathCountry,deathState,deathCity,nameFirst,nameLast,nameGiven,weight,height,bats,throws,debut,finalGame,retroID,bbrefID\n");
        }
        // First chunk should contain 10 lines (header + 10 data rows)
        String[] lines = chunks.get(0).split("\r?\n");
        assert lines.length == 11;
    }
    @Test
    void testSplitCsvIntoChunks() {
        String csv = "header1,header2\nrow1a,row1b\nrow2a,row2b\nrow3a,row3b\nrow4a,row4b\nrow5a,row5b";
        // Use the ChatClientService splitCsvIntoChunks method
        com.app.playerservicejava.service.chat.ChatClientService chatClientService = new com.app.playerservicejava.service.chat.ChatClientService();
        java.util.List<String> chunks = chatClientService.splitCsvIntoChunks(csv, 2);
        // There should be 3 chunks: rows 1-2, 3-4, 5
        assertNotNull(chunks);
        assert chunks.size() == 3;
        // Each chunk should start with the header
        for (String chunk : chunks) {
            assert chunk.startsWith("header1,header2\n");
        }
        // First chunk should contain rows 1 and 2
        assert chunks.get(0).contains("row1a,row1b");
        assert chunks.get(0).contains("row2a,row2b");
        // Second chunk should contain rows 3 and 4
        assert chunks.get(1).contains("row3a,row3b");
        assert chunks.get(1).contains("row4a,row4b");
        // Third chunk should contain row 5
        assert chunks.get(2).contains("row5a,row5b");
    }
   
    @Autowired
    private PlayerService playerService;

    @Test
    void contextLoads() {
    }

    @Test
    void testResourceAvailable() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("Player.csv");
        assertNotNull(is, "Player.csv should be available in the classpath");
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
