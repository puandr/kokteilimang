package com.example.cocktail_game;

import com.example.cocktail_game.domain.CocktailGame;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = "com.example.cocktail_game")
@SpringBootApplication
public class GuessTheCocktailGameApplication {

    private final CocktailGame cocktailGame;

    public GuessTheCocktailGameApplication(@Lazy CocktailGame cocktailGame) {
        this.cocktailGame = cocktailGame;
    }

    public static void main(String[] args) {
        SpringApplication.run(GuessTheCocktailGameApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try {
                cocktailGame.startGame();
            } catch (Exception e) {
                System.err.println("An error occurred while running the game: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        };
    }
}