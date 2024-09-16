package com.example.cocktail_game.model;

import lombok.*;
import java.util.Set;

@Getter
@Setter
public class GameState {
    private String gameId;
    private String hiddenName;
    private String actualName;
    private String instructions;
    private GameDifficulty difficulty;
    private int currentScore;
    private int highScore;
    private boolean newHighScore;
    private int numberOfCocktailsRevealed;
    private int maxAttempts;
    private boolean isGameOver;
    private Set<String> usedCocktails;
    private CocktailHint cocktailHint;
    private int revealedHintsCount;
    private String gameOverMessage;

    public GameState(String gameId, String hiddenName, String actualName, String instructions, GameDifficulty difficulty, int currentScore, int highScore, boolean newHighScore, int numberOfCocktailsRevealed, int maxAttempts, Set<String> usedCocktails, CocktailHint cocktailHint, int revealedHintsCount) {
        this.gameId = gameId;
        this.hiddenName = hiddenName;
        this.actualName = actualName;
        this.instructions = instructions;
        this.difficulty = difficulty;
        this.currentScore = currentScore;
        this.highScore = highScore;
        this.newHighScore = newHighScore;
        this.numberOfCocktailsRevealed = numberOfCocktailsRevealed;
        this.maxAttempts = maxAttempts;
        this.isGameOver = false;
        this.usedCocktails = usedCocktails;
        this.cocktailHint = cocktailHint;
        this.revealedHintsCount = revealedHintsCount;
    }
}