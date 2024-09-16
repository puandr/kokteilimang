package com.example.cocktail_game.service;

import com.example.cocktail_game.model.*;
import com.example.cocktail_game.repository.HighScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CocktailGameService {

    private final CocktailService cocktailService;
    private final HighScoreRepository highScoreRepository;
    private final Map<String, GameState> ongoingGames = new HashMap<>();
    private final int maxAttempts = 5;

    @Autowired
    public CocktailGameService(CocktailService cocktailService, HighScoreRepository highScoreRepository) {
        this.cocktailService = cocktailService;
        this.highScoreRepository = highScoreRepository;
    }

    public GameState startGame(GameSettings settings) {
        String gameId = UUID.randomUUID().toString();
        int highScore = highScoreRepository.getHighScore();
        int currentScore = 0;
        int numberOfCocktailsRevealed = 0;
        GameDifficulty gameDifficulty = settings.getDifficulty();
        GameState gameState = initializeNewRound(gameId, gameDifficulty, new HashSet<>(), currentScore, highScore, numberOfCocktailsRevealed);
        ongoingGames.put(gameId, gameState);
        return gameState;
    }

    private GameState initializeNewRound(String gameId, GameDifficulty difficulty, Set<String> usedCocktails, int currentScore, int highScore, int numberOfCocktailsRevealed) {
        Cocktail cocktail = cocktailService.getUniqueCocktail(usedCocktails);
        //TODO remove answer printing
//        System.out.println(cocktail.getName());
        String actualName = cocktail.getName();
        String hiddenName = getHiddenName(cocktail.getName());
        boolean newHighScore = false;
        int revealedHintsCount = 0;

        CocktailHint cocktailHint = new CocktailHint(
                cocktail.getCategory(),
                cocktail.getGlass(),
                cocktail.getAlcoholic(),
                cocktail.getIngredients()
        );

        return new GameState(
                gameId,
                hiddenName,
                actualName,
                cocktail.getInstructions(),
                difficulty,
                currentScore,
                highScore,
                newHighScore,
                numberOfCocktailsRevealed,
                maxAttempts,
                usedCocktails,
                cocktailHint,
                revealedHintsCount // No hints revealed at start
        );
    }

    public GameState processGuess(String gameId, Guess guess) {
        GameState gameState = ongoingGames.get(gameId);

        if (gameState == null) {
            throw new RuntimeException("Game not found");
        }

        if (gameState.isGameOver()) {
            return gameState;
        }

        String actualName = revealLetter(gameState.getHiddenName(), gameState.getActualName(), gameState.getDifficulty().getLettersToReveal());

        if (actualName == null) {
            gameState.setGameOver(true);
            gameState.setGameOverMessage("No more letters can be revealed. \n\n" + gatherEndGameStatistics(gameState));
            return gameState;
        }

        if (guess.getGuess().equalsIgnoreCase(gameState.getActualName())) {
            gameState.setCurrentScore(gameState.getCurrentScore() + gameState.getMaxAttempts());
            gameState.setNumberOfCocktailsRevealed(gameState.getNumberOfCocktailsRevealed() + 1);
            gameState.getUsedCocktails().add(gameState.getActualName());
            GameState newGameState = initializeNewRound(gameId, gameState.getDifficulty(), gameState.getUsedCocktails(), gameState.getCurrentScore(), gameState.getHighScore(), gameState.getNumberOfCocktailsRevealed());
            ongoingGames.put(gameId, newGameState);
            return newGameState;
        } else {
            gameState.setMaxAttempts(gameState.getMaxAttempts() - 1);
            if (gameState.getMaxAttempts() > 0) {
                gameState.setRevealedHintsCount(Math.min(gameState.getRevealedHintsCount() + 1, 4));
            } else {
                gameState.setGameOver(true);
                gameState.setGameOverMessage(gatherEndGameStatistics(gameState));
                checkForNewHighScore(gameState);
            }
        }

        gameState.setHiddenName(actualName);
        return gameState;
    }

    public void checkForNewHighScore(GameState gameState) {
        if (gameState != null && gameState.getCurrentScore() > highScoreRepository.getHighScore()) {
            highScoreRepository.saveHighScore(gameState.getCurrentScore());
        }
    }

    public String gatherEndGameStatistics(GameState gameState) {
        StringBuilder message = new StringBuilder();
        message.append("Game Over!\n");
        message.append("You revealed ").append(gameState.getNumberOfCocktailsRevealed()).append(" cocktails.\n");
        message.append("The cocktail you couldn't guess was ").append(gameState.getActualName()).append(".\n");
        message.append("Your final score is ").append(gameState.getCurrentScore()).append(".\n");

        if (gameState.getCurrentScore() > gameState.getHighScore()) {
            message.append("Congratulations! You've achieved a new high score!\n");
        } else {
            message.append("Current high score is ").append(gameState.getHighScore()).append(".");
        }

        return message.toString();
    }

    public GameState skipRound(String gameId) {
        GameState gameState = ongoingGames.get(gameId);
        if (gameState == null) {
            throw new RuntimeException("Game not found");
        }

        gameState = initializeNewRound(gameId, gameState.getDifficulty(), gameState.getUsedCocktails(), gameState.getCurrentScore(), gameState.getHighScore(), gameState.getNumberOfCocktailsRevealed());
        ongoingGames.put(gameId, gameState);

        return gameState;
    }

    private String getHiddenName(String name) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                output.append(" ");
            } else {
                output.append("_");
            }
        }
        return output.toString();
    }

    private String revealLetter(String hiddenName, String actualName, int numLettersToReveal) {
        StringBuilder revealed = new StringBuilder(hiddenName);
        List<Integer> unrevealedIndices = new ArrayList<>();

        for (int i = 0; i < actualName.length(); i++) {
            if (hiddenName.charAt(i) == '_' && Character.isLetter(actualName.charAt(i))) {
                unrevealedIndices.add(i);
            }
        }

        if (unrevealedIndices.size() <= numLettersToReveal) {
            return null;
        }

        Random random = new Random();
        for (int i = 0; i < numLettersToReveal; i++) {
            int randomIndex = random.nextInt(unrevealedIndices.size());
            int letterIndex = unrevealedIndices.get(randomIndex);

            revealed.setCharAt(letterIndex, actualName.charAt(letterIndex));
            unrevealedIndices.remove(randomIndex);
        }
        return revealed.toString();
    }

    public String getRules() {
        String rules = "Welcome to the Guess the Cocktail Game! Here are the rules:\n" +
                "You will be given the instructions for a cocktail.\n" +
                "You have " + maxAttempts +" attempts to guess the name of the cocktail.\n" +
                "If answered correctly the game continues with a new random cocktail and score is increased by number of attempts left.\n" +
                "If your guess is wrong, some letter(s) and one hint will be revealed.\n" +
                "Your answers are case insensitive.\n" +
                "You can skip a round if you're stuck.";
        return rules;
    }

}
