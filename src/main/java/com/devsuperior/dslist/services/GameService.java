package com.devsuperior.dslist.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.errors.AppError;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameRepository;

@Service
public class GameService {

  @Autowired // para o Spring fazer a injeção de dependência
  private GameRepository gameRepository;
  
  @Transactional(readOnly = true)
  public List<GameMinDTO> findAll() {
    List<Game> result = gameRepository.findAll();

    return result.stream().map(x -> new GameMinDTO(x)).toList(); // para converter cada elemento da lista result em um GameMinDTO e depois retornar uma lista de GameMinDTO
  }

  @Transactional(readOnly = true)
  public GameDTO findById(Long id) {
      Optional<Game> game = gameRepository.findById(id);
  
      if (game.isPresent()) {
          Game result = game.get();
          GameDTO dto = new GameDTO(result);

          return dto;
      } else {
          throw new AppError("Game not found", 404);
      }
  }

  @Transactional(readOnly = true)
  public List<GameMinDTO> findByList(Long listId) {
    List<GameMinProjection> result = gameRepository.searchByList(listId);

    if (result.isEmpty()) {
      throw new AppError("List not found", 404);
    }

    return result.stream().map(x -> new GameMinDTO(x)).toList(); // para converter cada elemento da lista result em um GameMinDTO e depois retornar uma lista de GameMinDTO
  }
}
