# Guess the Cocktail Game

Welcome to the "Guess the Cocktail Game"! This is a console-based game where you try to guess the name of a cocktail based on its instructions. With each incorrect guess, you'll receive additional hints to help you figure out the correct answer.

## Features

- Fetches random cocktail recipes from TheCocktailDB API.
- Provides hints such as ingredients, category, glass type, and whether the cocktail is alcoholic.
- Allows setting the game difficulty, which determines the number of letters revealed after each incorrect guess.
- Keeps track of the high score locally.

## Technologies Used

- **Java**: The primary programming language used to build the game.
- **Spring Boot**: For building the backend logic and handling API interactions.
- **Maven**: For managing dependencies and building the project.
- **TheCocktailDB API**: Used to fetch random cocktail recipes.
- **JUnit & Mockito**: For unit testing the game logic.

## How to Play

1. **Start the Game**: The game begins with a welcome message and rules displayed on the console.
2. **Select Difficulty**: Choose your desired difficulty level:
    - **Easy**: 3 letters are revealed with each incorrect guess.
    - **Normal**: 2 letters are revealed.
    - **Hard**: Only 1 letter is revealed.
3. **Guess the Cocktail**: Based on the instructions provided, try to guess the cocktail's name.
4. **Hints**: With each incorrect guess, additional hints such as the type of glass, ingredients, and whether the drink is alcoholic will be revealed.
5. **Scoring**: Earn points based on the number of attempts left when you guess correctly. The game tracks the high score, so try to beat your best!

## Getting Started

### Prerequisites

- Java 21 or later
- Maven

### Running the Game

1. Clone the repository to your local machine:
    ```bash
    git clone https://github.com/your-username/guess-the-cocktail-game.git
    ```
2. Navigate to the project directory:
    ```bash
    cd guess-the-cocktail-game
    ```
3. Build the project using Maven:
    ```bash
    mvn clean install
    ```
4. Run the game:
    ```bash
    mvn spring-boot:run
    ```

### Playing the Game

After running the game, follow the on-screen instructions to play. You'll start by selecting a difficulty level, and then the game will begin giving you hints for guessing the cocktail name. Try to guess correctly in as few attempts as possible to maximize your score!

---

Enjoy the game, and good luck guessing the cocktails!
