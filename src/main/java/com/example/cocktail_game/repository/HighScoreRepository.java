package com.example.cocktail_game.repository;

public interface HighScoreRepository {
    int getHighScore();
    void saveHighScore(int score);
}
