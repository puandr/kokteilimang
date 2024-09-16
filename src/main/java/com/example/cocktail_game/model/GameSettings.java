package com.example.cocktail_game.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameSettings {
    private GameDifficulty difficulty;

    public GameSettings() {
    }

    public GameSettings(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }
}
