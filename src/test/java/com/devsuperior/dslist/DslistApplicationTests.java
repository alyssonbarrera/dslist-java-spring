package com.devsuperior.dslist;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.devsuperior.dslist.dto.GameDTO;
import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.Game;
import com.devsuperior.dslist.dto.GameMinDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.entities.Belonging;
import com.devsuperior.dslist.services.GameListService;
import com.devsuperior.dslist.services.GameService;
import com.devsuperior.dslist.repositories.GameRepository;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.BelongingRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    public void testGameServiceFindAll() {
        List<Game> games = Arrays.asList(
            new Game((long) 1, "Game 1", 2023, "action", "netflix", "", "short description", "long description"),
            new Game((long) 2, "Game 2", 2023, "action", "netflix", "", "short description", "long description"),
            new Game((long) 3, "Game 3", 2023, "action", "netflix", "", "short description", "long description")
        );

        gameRepository.saveAll(games);

        List<GameMinDTO> result = gameService.findAll();

        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).getId() + " " + result.get(i).getTitle() + " " + result.get(i));
        }
        
		assertTrue(result.stream().allMatch(game -> game.getId() != null));
		assertTrue(result.stream().findFirst().get().getTitle().equals("Game 1"));
    }

    @Test
    public void testGameServiceFindById() {
        Game game = new Game((long) 1, "Game 1", 2023, "action", "netflix", "", "short description", "long description");

        Game savedGame = gameRepository.save(game);
        
        GameDTO result = gameService.findById((long) savedGame.getId()); 

        assertTrue(result.getTitle().equals("Game 1"));
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(new GameDTO(savedGame));
    }

    @Test
    public void testGameServiceFindByList() {
        List<Game> games = Arrays.asList(
            new Game((long) 1, "Game 1", 2023, "action", "netflix", "", "short description", "long description"),
            new Game((long) 2, "Game 2", 2023, "action", "netflix", "", "short description", "long description"),
            new Game((long) 3, "Game 3", 2023, "action", "netflix", "", "short description", "long description")
            );

        GameList gameList = new GameList("Game List 1");

        Belonging belonging1 = new Belonging(games.get(0), gameList, 0);
        Belonging belonging2 = new Belonging(games.get(1), gameList, 1);
        Belonging belonging3 = new Belonging(games.get(2), gameList, 2);

        GameList savedGameList = gameListRepository.save(gameList);
        belongingRepository.saveAll(Arrays.asList(belonging1, belonging2, belonging3));
        gameRepository.saveAll(games);

        List<GameMinDTO> result = gameService.findByList(savedGameList.getId());

        assertTrue(result.size() == 3);
        assertTrue(result.stream().findFirst().get().getTitle().equals("Game 1"));
    }

    @Test
    public void testGameListService() {
        GameList gameList = new GameList((long) 1, "Game List 1");

        GameList savedGameList = gameListRepository.save(gameList);

        List<GameListDTO> result = gameListService.findAll();

        assertTrue(result.stream().findFirst().get().getId() == savedGameList.getId());
        assertTrue(result.stream().findFirst().get().getName().equals("Game List 1"));
    }
}
