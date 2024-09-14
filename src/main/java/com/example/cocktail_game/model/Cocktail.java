package com.example.cocktail_game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cocktail {
    @JsonProperty("strDrink")
    private String name;

    @JsonProperty("strInstructions")
    private String instructions;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strGlass")
    private String glass;

    private List<String> ingredients = new ArrayList<>();

    @JsonProperty("strAlcoholic")
    private String alcoholic;

    @JsonProperty("strDrinkThumb")
    private String picture;
}
