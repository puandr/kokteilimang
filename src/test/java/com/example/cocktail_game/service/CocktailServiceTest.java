package com.example.cocktail_game.service;

import com.example.cocktail_game.model.Cocktail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CocktailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CocktailService cocktailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRandomCocktail_Success() throws Exception {
        String mockApiResponse = "{ \"drinks\": [{ \"strDrink\": \"Margarita\", \"strIngredient1\": \"Tequila\" }] }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockApiResponse);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(mockApiResponse)).thenReturn(mockJsonNode);
        when(mockJsonNode.get("drinks")).thenReturn(mockJsonNode);
        when(mockJsonNode.get(0)).thenReturn(mockJsonNode);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        when(objectMapper.treeToValue(mockJsonNode, Cocktail.class)).thenReturn(mockCocktail);

        Cocktail cocktail = cocktailService.getRandomCocktail();

        assertNotNull(cocktail);
        assertEquals("Margarita", cocktail.getName());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(objectMapper, times(1)).readTree(mockApiResponse);
    }

    @Test
    public void testGetRandomCocktail_NullResponse() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        Cocktail cocktail = cocktailService.getRandomCocktail();

        assertNull(cocktail);
    }

    @Test
    public void testGetRandomCocktail_InvalidJson() throws Exception {
        String invalidJson = "{ invalid_json_response }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(invalidJson);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(invalidJson)).thenReturn(mockJsonNode);
        when(mockJsonNode.get("drinks")).thenReturn(null);

        Cocktail cocktail = cocktailService.getRandomCocktail();

        assertNull(cocktail);
    }

    @Test
    public void testGetUniqueCocktail_Success() throws Exception {
        String mockApiResponse = "{ \"drinks\": [{ \"strDrink\": \"Margarita\", \"strIngredient1\": \"Tequila\" }] }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockApiResponse);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(mockApiResponse)).thenReturn(mockJsonNode);

        JsonNode drinksNode = mock(JsonNode.class);
        when(mockJsonNode.get("drinks")).thenReturn(drinksNode);
        when(drinksNode.get(0)).thenReturn(mockJsonNode);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        when(objectMapper.treeToValue(mockJsonNode, Cocktail.class)).thenReturn(mockCocktail);

        Set<String> usedCocktails = new HashSet<>();

        Cocktail uniqueCocktail = cocktailService.getUniqueCocktail(usedCocktails);

        assertNotNull(uniqueCocktail);
        assertEquals("Margarita", uniqueCocktail.getName());
    }

    @Test
    public void testGetUniqueCocktail_RetriesAndFails() throws Exception {
        String mockApiResponse = "{ \"drinks\": [{ \"strDrink\": \"Margarita\", \"strIngredient1\": \"Tequila\" }] }";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockApiResponse);

        JsonNode mockJsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(mockApiResponse)).thenReturn(mockJsonNode);

        JsonNode drinksNode = mock(JsonNode.class);
        when(mockJsonNode.get("drinks")).thenReturn(drinksNode);
        when(drinksNode.get(0)).thenReturn(mockJsonNode);

        Cocktail mockCocktail = new Cocktail();
        mockCocktail.setName("Margarita");
        when(objectMapper.treeToValue(mockJsonNode, Cocktail.class)).thenReturn(mockCocktail);

        Set<String> usedCocktails = new HashSet<>();
        usedCocktails.add("margarita");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cocktailService.getUniqueCocktail(usedCocktails);
        });

        int expectedRetryCount = cocktailService.getNumberOfRetriesToGetUniqueCocktail();

        assertEquals("Unable to find a unique cocktail after " + expectedRetryCount + " attempts.", exception.getMessage());
    }
}

