package com.app.playerservicejava.controller;

import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.ResponseEntity.ok;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.playerservicejava.model.Player;
import com.app.playerservicejava.model.Players;
import com.app.playerservicejava.service.PlayerService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(value = "${api.version}/players", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PlayerController {
    @Resource
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Players> getPlayers(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Players players = playerService.getPlayers(page, size);
        return ok(players);
    }

    /**
     * Get player by id.
     * This method retrieves a player by their ID from the service.
     * It returns a 200 OK response with the player data if found, or a 404 Not
     * Found response if not found.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") String id) {
        Optional<Player> player = playerService.getPlayerById(id);

        if (player.isPresent()) {
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get players by birthCountry.
     * This method retrieves all players by their birthCountry from the service.
     * It returns a 200 OK response with the list of players (may be empty).
     * 
     * @param birthCountry
     * @return
     */
    @GetMapping("/birthCountry/{birthCountry}")
    public ResponseEntity<Players> getPlayersByBirthCountry(@PathVariable("birthCountry") String birthCountry,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Players players = playerService.getPlayersByBirthCountry(birthCountry, page, size);
        if (players.getPlayers().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(players, HttpStatus.OK);
        }
    }
}
