package com.devsuperior.dslist.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.services.GameService;

@RestController
@RequestMapping(value = "/games")
public class GameController {
  
  @Autowired
  private GameService gameService;

  @GetMapping
  public ResponseEntity<List<GameMinDTO>> findAll() {
    List<GameMinDTO> result = gameService.findAll();
    
    return ResponseEntity.ok().body(result);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    GameDTO result = gameService.findById(id);
    
    return ResponseEntity.ok().body(result);
  }
}
