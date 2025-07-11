package com.app.playerservicejava.repository;

import com.app.playerservicejava.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerRepository extends JpaRepository<Player, String> {
    Page<Player> findByBirthCountry(String birthCountry, Pageable pageable);
}
