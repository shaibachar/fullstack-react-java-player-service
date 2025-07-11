package com.app.playerservicejava.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.repository.PlayerRepository;

@Service
public class PlayerService {
    @Value("${pagination.default-page:0}")
    private int defaultPage;
    @Value("${pagination.default-size:10}")
    private int defaultSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private PlayerRepository playerRepository;

    public Players getPlayers(Integer page, Integer size) {
        int pageNum = (page == null) ? defaultPage : page;
        int pageSize = (size == null) ? defaultSize : size;
        Players players = new Players();
        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<Player> playerPage = playerRepository.findAll(pageable);
            playerPage.forEach(players.getPlayers()::add);
        } catch (Exception e) {
            LOGGER.error("Exception in getPlayers: {}", e.toString());
            return new Players();
        }
        return players;
    }

    /**
     * Get player by birthCountry.
     * This method simulates a network delay to mimic real-world scenarios.
     * It retrieves a player by their birthCountry from the repository.
     * 
     * @param playerId
     * @return
     */

    public Players getPlayersByBirthCountry(String birthCountry, Integer page, Integer size) {
        int pageNum = (page == null) ? defaultPage : page;
        int pageSize = (size == null) ? defaultSize : size;
        Players players = new Players();
        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<Player> playerPage = playerRepository.findByBirthCountry(birthCountry, pageable);
            playerPage.forEach(players.getPlayers()::add);
        } catch (Exception e) {
            LOGGER.error("Exception in getPlayersByBirthCountry: {}", e.toString());
            return new Players();
        }
        return players;
    }

    /**
     * Get player by ID.
     * This method simulates a network delay to mimic real-world scenarios.
     * It retrieves a player by their ID from the repository.
     * 
     * @param playerId
     * @return
     */
    public Optional<Player> getPlayerById(String playerId) {
        Optional<Player> player = null;

        try {
            player = playerRepository.findById(playerId);
        } catch (Exception e) {
            LOGGER.error("message=Exception in getPlayerById; exception={}", e.toString());
            return Optional.empty();
        }
        return player;
    }

}
