package com.devsuperior.dslist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.Game;
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

  @Transactional(readOnly = true) // para garantir que toda a operação com o banco seja resolvida no service e o readOnly = true para não travar o banco de dados
  public GameDTO findById(Long id) {
      Game result = gameRepository.findById(id).get();
      GameDTO dto = new GameDTO(result);
      
      return dto;
  }

  @Transactional(readOnly = true)
  public List<GameMinDTO> findByList(Long listId) {
    List<GameMinProjection> result = gameRepository.searchByList(listId);

    return result.stream().map(x -> new GameMinDTO(x)).toList(); // para converter cada elemento da lista result em um GameMinDTO e depois retornar uma lista de GameMinDTO
  }
}
