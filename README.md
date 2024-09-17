# Guess the Cocktail Game (Web Version)

Welcome to the "Guess the Cocktail Game"! This is a web-based game where you try to guess the name of a cocktail based on its instructions. With each incorrect guess, you'll receive additional hints to help you figure out the correct answer.

## Features

- Fetches random cocktail recipes from TheCocktailDB API.
- Provides hints such as ingredients, category, glass type, and whether the cocktail is alcoholic.
- Allows setting the game difficulty, which determines the number of letters revealed after each incorrect guess.
- Keeps track of the high score locally.
- Interactive web interface built with React.

## Technologies Used

- **Java**: The backend logic for the game.
- **Spring Boot**: For building the backend API and handling interactions with TheCocktailDB API.
- **React**: The frontend framework used to build the user interface.
- **Maven**: For managing backend dependencies and building the project.
- **TheCocktailDB API**: Used to fetch random cocktail recipes.

## How to Play

1. **Start the Game**: The game begins with a welcome message and rules displayed on the web page.
2. **Select Difficulty**: Choose your desired difficulty level:
    - **Easy**: 3 letters are revealed with each incorrect guess.
    - **Normal**: 2 letters are revealed.
    - **Hard**: Only 1 letter is revealed.
3. **Guess the Cocktail**: Based on the instructions provided, try to guess the cocktail's name.
4. **Hints**: With each incorrect guess, additional hints such as the type of glass, ingredients, and whether the drink is alcoholic will be revealed.
5. **Scoring**: Earn points based on the number of attempts left when you guess correctly. The game tracks the high score, so try to beat your best!

## Getting Started

### Prerequisites

- **Backend**: Java 21 or later, Maven
- **Frontend**: Node.js, npm

### Running the Game

#### Backend

1. Clone the repository to your local machine:
    ```bash
    git clone https://github.com/your-username/guess-the-cocktail-game.git
    ```
2. Navigate to the backend project directory:
    ```bash
    cd guess-the-cocktail-game
    ```
3. Build the backend project using Maven:
    ```bash
    mvn clean install
    ```
4. Run the Spring Boot backend:
    ```bash
    mvn spring-boot:run
    ```

#### Frontend

1. Navigate to the frontend directory:
    ```bash
    cd frontend
    ```
2. Install the required dependencies:
    ```bash
    npm install
    ```
3. Start the React development server:
    ```bash
    npm start
    ```
4. Open a browser and go to `http://localhost:3000` to play the game.
