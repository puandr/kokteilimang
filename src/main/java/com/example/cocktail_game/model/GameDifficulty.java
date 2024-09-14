package com.example.cocktail_game.model;

import lombok.Getter;

@Getter
public enum GameDifficulty {
    EASY(3),
    NORMAL(2),
    HARD(1);

    private final int lettersToReveal;

    GameDifficulty(int lettersToReveal) {
        this.lettersToReveal = lettersToReveal;
    }

}
