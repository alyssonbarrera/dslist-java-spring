package com.devsuperior.dslist;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.entities.Belonging;
import com.devsuperior.dslist.services.GameService;
import com.devsuperior.dslist.services.GameListService;
import com.devsuperior.dslist.repositories.GameRepository;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.BelongingRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DslistApplicationTests {

	@Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameListRepository gameListRepository;

    @Autowired
    private BelongingRepository belongingRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameListService gameListService;

    @BeforeEach
    public void setUp() {
        belongingRepository.deleteAll();
        gameListRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testGameServiceFindAll() {
        List<Game> games = Arrays.asList(
            new Game((long) 1, "Game 1", 2023, "action", "PC", "", "short description", "long description"),
            new Game((long) 2, "Game 2", 2023, "action", "PC", "", "short description", "long description"),
            new Game((long) 3, "Game 3", 2023, "action", "PC", "", "short description", "long description")
        );

        gameRepository.saveAll(games);

        List<GameMinDTO> result = gameService.findAll();

        for (GameMinDTO gameMinDTO : result) {
            System.out.println(gameMinDTO.getId() + " " + gameMinDTO.getTitle() + " " + gameMinDTO);
        }

        assertEquals(3, result.size());
		assertTrue(result.stream().allMatch(game -> game.getId() != null));
        assertEquals("Game 1", result.stream().findFirst().get().getTitle());
    }

    @Test
    public void testGameServiceFindById() {
        Game game = new Game((long) 1, "Game 1", 2023, "action", "PC", "", "short description", "long description");

        Game savedGame = gameRepository.save(game);
        
        GameDTO result = gameService.findById((long) savedGame.getId());

        assertEquals("Game 1", result.getTitle());
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(new GameDTO(savedGame));
    }

    @Test
    public void testGameServiceFindByList() {
        List<Game> games = Arrays.asList(
            new Game((long) 1, "Game 1", 2023, "action", "PC", "", "short description", "long description"),
            new Game((long) 2, "Game 2", 2023, "action", "PC", "", "short description", "long description"),
            new Game((long) 3, "Game 3", 2023, "action", "PC", "", "short description", "long description")
        );
            
        List<Game> savedGames = gameRepository.saveAll(games);
        
        GameList gameList = new GameList("Game List 1");        
        GameList savedGameList = gameListRepository.save(gameList);

        Belonging belonging1 = new Belonging(savedGames.get(0), gameList, 0);
        Belonging belonging2 = new Belonging(savedGames.get(1), gameList, 1);
        Belonging belonging3 = new Belonging(savedGames.get(2), gameList, 2);

        belongingRepository.saveAll(Arrays.asList(belonging1, belonging2, belonging3));

        List<GameMinDTO> result = gameService.findByList((long) savedGameList.getId());

        assertEquals(3, result.size());
        assertEquals("Game 1", result.stream().findFirst().get().getTitle());
    }

    @Test
    public void testGameListService() {
        GameList gameList = new GameList((long) 1, "Game List 1");

        GameList savedGameList = gameListRepository.save(gameList);

        List<GameListDTO> result = gameListService.findAll();

        assertEquals(1, result.size());
        assertSame(result.stream().findFirst().get().getId(), savedGameList.getId());
        assertEquals("Game List 1", result.stream().findFirst().get().getName());
    }
}
