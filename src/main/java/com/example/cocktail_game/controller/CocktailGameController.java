package com.example.cocktail_game.controller;

import com.example.cocktail_game.model.GameSettings;
import com.example.cocktail_game.model.GameState;
import com.example.cocktail_game.model.Guess;
import com.example.cocktail_game.service.CocktailGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cocktailgame")
public class CocktailGameController {

    private final CocktailGameService cocktailGameService;

    @Autowired
    public CocktailGameController(CocktailGameService cocktailGameService) {
        this.cocktailGameService = cocktailGameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameState> startGame(@RequestBody GameSettings settings) {
        GameState gameState = cocktailGameService.startGame(settings);
        return ResponseEntity.ok(gameState);
    }

    @PostMapping("/{gameId}/guess")
    public ResponseEntity<GameState> makeGuess(@PathVariable String gameId, @RequestBody Guess guess) {
        GameState gameState = cocktailGameService.processGuess(gameId, guess);
        return ResponseEntity.ok(gameState);
    }

    @PostMapping("/{gameId}/skip")
    public ResponseEntity<GameState> skipRound(@PathVariable String gameId) {
        GameState gameState = cocktailGameService.skipRound(gameId);
        return ResponseEntity.ok(gameState);
    }

    @GetMapping("/rules")
    public ResponseEntity<String> getRules() {
        return ResponseEntity.ok(cocktailGameService.getRules());
    }
}
