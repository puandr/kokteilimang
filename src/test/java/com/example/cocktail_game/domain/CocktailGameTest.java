package com.example.cocktail_game.domain;

import com.example.cocktail_game.model.Cocktail;
import com.example.cocktail_game.model.GameDifficulty;
import com.example.cocktail_game.repository.HighScoreRepository;
import com.example.cocktail_game.service.CocktailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class CocktailGameTest {

    @Mock
    private CocktailService cocktailService;

    @Mock
    private HighScoreRepository highScoreRepository;

    @InjectMocks
    private CocktailGame cocktailGame;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        cocktailGame = new CocktailGame(cocktailService, highScoreRepository);
    }

    @Test
    public void testGameInitialization() {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        String userInput = "2\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.initializeGame();

        assertEquals(GameDifficulty.NORMAL, cocktailGame.getGameDifficulty());
        assertEquals(0, cocktailGame.getCurrentScore());
        assertTrue(cocktailGame.isGameOngoing());
    }


    @Test
    public void testPlayRoundWithCorrectGuess() {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        mockCocktail.setInstructions("Mix and serve.");

        when(cocktailService.getUniqueCocktail(anySet())).thenReturn(mockCocktail);

        cocktailGame.setGameDifficulty(GameDifficulty.NORMAL);
        String userInput = "2\nMargarita\na\na\na\na\na\nn\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.startGame();

        assertEquals(5, cocktailGame.getCurrentScore());
    }


    @Test
    public void testPlayRoundWithIncorrectGuesses() throws Exception {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        mockCocktail.setInstructions("Mix and serve.");

        when(cocktailService.getUniqueCocktail(anySet())).thenReturn(mockCocktail);

        cocktailGame.setGameDifficulty(GameDifficulty.NORMAL);
        String userInput = "2\na\na\nMargarita\na\na\na\na\nn\nn\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.startGame();

        assertEquals(3, cocktailGame.getCurrentScore());
    }

    @Test
    public void testEndGame() throws Exception {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        mockCocktail.setInstructions("Mix and serve.");

        when(cocktailService.getUniqueCocktail(anySet())).thenReturn(mockCocktail);

        cocktailGame.setGameDifficulty(GameDifficulty.NORMAL);
        String userInput = "2\na\na\na\na\na\nn\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.startGame();

        assertEquals(0, cocktailGame.getCurrentScore());
    }

    @Test
    public void testSetGameDifficultyWithInvalidNumericInput() {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        mockCocktail.setInstructions("Mix and serve.");

        when(cocktailService.getUniqueCocktail(anySet())).thenReturn(mockCocktail);

        cocktailGame.setGameDifficulty(GameDifficulty.NORMAL);
        String userInput = "7\na\na\na\na\na\nn\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.startGame();

        assertEquals(GameDifficulty.NORMAL, cocktailGame.getGameDifficulty());
    }

    @Test
    public void testSetGameDifficultyWithInvalidLiteralInput() {
        when(highScoreRepository.getHighScore()).thenReturn(0);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        mockCocktail.setInstructions("Mix and serve.");

        when(cocktailService.getUniqueCocktail(anySet())).thenReturn(mockCocktail);

        cocktailGame.setGameDifficulty(GameDifficulty.NORMAL);
        String userInput = "abc\na\na\na\na\na\nn\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(userInput.getBytes()));

        cocktailGame.setScanner(testScanner);
        cocktailGame.startGame();

        assertEquals(GameDifficulty.NORMAL, cocktailGame.getGameDifficulty());
    }

    //TODO more tests
    //Some other test, that could be implemented
    @Test
    public void testRevealLetterEasyDifficulty() {}

    @Test
    public void testRevealLetterHardDifficulty() {}

    @Test
    public void testEmptyPlayerGuess() {}

}