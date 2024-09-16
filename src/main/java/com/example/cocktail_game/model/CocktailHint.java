package com.example.cocktail_game.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CocktailHint {
    private String category;
    private String glass;
    private String alcoholic;
    private List<String> ingredients;

    public CocktailHint(String category, String glass, String alcoholic, List<String> ingredients) {
        this.category = category;
        this.glass = glass;
        this.alcoholic = alcoholic;
        this.ingredients = ingredients;
    }
}
