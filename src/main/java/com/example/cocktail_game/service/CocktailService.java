
package com.example.cocktail_game.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.cocktail_game.model.Cocktail;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Service
public class CocktailService {
    private final String COCKTAILDB_API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final int numberOfRetriesToGetUniqueCocktail = 3;

    public CocktailService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    //TODO implement logger
    public Cocktail getRandomCocktail() {
        try {
            String responseJson = restTemplate.getForObject(COCKTAILDB_API_URL, String.class);

            if (responseJson == null) {
                throw new RestClientException("API returned null response");
            }

            JsonNode rootNode = objectMapper.readTree(responseJson);
            JsonNode drinksNode = rootNode.get("drinks").get(0);

            Cocktail cocktail = objectMapper.treeToValue(drinksNode, Cocktail.class);
            cocktail.setIngredients(extractIngredients(drinksNode));

            return cocktail;
        } catch (RestClientException e) {
            System.err.println("Error fetching data from the API: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Error processing the API response: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private List<String> extractIngredients(JsonNode drinksNode) {
        List<String> ingredients = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            String ingredientKey = "strIngredient" + i;
            JsonNode ingredientNode = drinksNode.get(ingredientKey);

            if (ingredientNode != null && !ingredientNode.asText().isEmpty() && !ingredientNode.asText().equals("null")) {
                ingredients.add(ingredientNode.asText());
            }
        }
        return ingredients;
    }

    public Cocktail getUniqueCocktail(Set<String> usedCocktails) {
        Cocktail cocktail;

        int retryCount = numberOfRetriesToGetUniqueCocktail;

        while (retryCount > 0) {
            cocktail = getRandomCocktail();

            if (cocktail == null || cocktail.getName() == null || cocktail.getName().equalsIgnoreCase("skip")) {
                retryCount--;
                continue;
            }

            String cocktailName = cocktail.getName().toLowerCase();

            if (usedCocktails.contains(cocktailName)) {
                retryCount--;
                continue;
            }
            return cocktail;
        }
        throw new RuntimeException("Unable to find a unique cocktail after " + numberOfRetriesToGetUniqueCocktail + " attempts.");
    }
}
