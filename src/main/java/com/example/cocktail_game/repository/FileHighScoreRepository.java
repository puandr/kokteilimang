package com.example.cocktail_game.repository;

import org.springframework.stereotype.Repository;

import java.io.*;

@Repository
public class FileHighScoreRepository implements HighScoreRepository{
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    @Override
    public int getHighScore() {
        File file = new File(HIGH_SCORE_FILE);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error reading high score from file.");
            }
        }
        return 0;
    }

    @Override
    public void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            System.out.println("Error saving high score to file.");
        }
    }
}
