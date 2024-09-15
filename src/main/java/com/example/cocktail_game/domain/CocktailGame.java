package com.example.cocktail_game.domain;

import com.example.cocktail_game.model.Cocktail;
import com.example.cocktail_game.model.GameDifficulty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.cocktail_game.repository.HighScoreRepository;
import com.example.cocktail_game.service.CocktailService;
import java.util.*;

@Getter
@Component
public class CocktailGame {
    private final CocktailService cocktailService;
    private final HighScoreRepository highScoreRepository;
    private Scanner scanner;
    private int highScore;
    private final int maxAttempts = 5;
    private int currentScore;
    private int revealedCocktails;
    boolean isGameOngoing;
    private GameDifficulty gameDifficulty;
    private HashSet<String> cocktailsBeenInTheGame;
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    @Autowired
    public CocktailGame(CocktailService cocktailService, HighScoreRepository highScoreRepository) {
        this.cocktailService = cocktailService;
        this.highScoreRepository = highScoreRepository;
        this.scanner = new Scanner(System.in);
        this.highScore = highScoreRepository.getHighScore();
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    private void saveHighScore(int score) {
        highScoreRepository.saveHighScore(score);
    }

    private void checkHighScore() {
        if (currentScore > highScore) {
            System.out.println("Congratulations! Your score is new high score!");
            System.out.println();
            highScore = currentScore;
            saveHighScore(highScore);
        } else {
            System.out.println("Your score: " + currentScore);
            System.out.println("Current high score is: " + highScore);
        }
    }

// TODO add tests
    public void startGame() {
        do {
            initializeGame();

            while (isGameOngoing) {
                playRound();
            }
        } while (isPlayAgain());
    }

    public void initializeGame() {
        cocktailsBeenInTheGame = new HashSet<>();
        currentScore = 0;
        isGameOngoing = true;

        printRules();
        setGameDifficulty();
        printHighScore();
    }

    private boolean isPlayAgain() {
        System.out.println();
        System.out.print("Do you want to play again? Enter \"Y\" to play again, anything else to exit ");
        String playAgainInput = scanner.nextLine().toLowerCase();
        return playAgainInput.equals("y");
    }

    public void playRound() {
        int currentAttempt = 1;
        int scoreForRound;
        String hiddenName;
        String cocktailName;

        Cocktail cocktail = getNewCocktail();
        cocktailName = cocktail.getName().toLowerCase();
        hiddenName = getHiddenName(cocktailName);

        printCocktailInstructions(cocktail);

        while (currentAttempt <= maxAttempts) {
            //TODO remove answer
//            System.out.println("Cocktail: " + cocktailName);
            printRoundIntro(hiddenName, currentAttempt);
            String playerGuess = askForPlayerGuess().toLowerCase();

            if (playerGuess.equals("skip")) {
                System.out.println("You've chosen to skip this round.");
                return; // Exit the playRound() method to start a new round
            }

            if (playerGuess.equals(cocktailName)) {
                //TODO get rid of magic number or ...
                scoreForRound = maxAttempts - currentAttempt + 1;
                currentScore = currentScore + scoreForRound;
                revealedCocktails++;

                System.out.println("That is correct! You've got " + scoreForRound + " points.");
                printCurrentScore();
                return;
            } else {
                if (revealLetter(hiddenName, cocktailName, gameDifficulty.getLettersToReveal()) == null) {
                    currentAttempt = maxAttempts + 1;
                    System.out.println("Not correct and there are not enough letters to reveal. You've got 0 points.");
                    continue;
                }
                hiddenName = revealLetter(hiddenName, cocktailName, gameDifficulty.getLettersToReveal());
                    if (currentAttempt != maxAttempts) {
                        System.out.println("Incorrect. But here is your additional hint:");
                        printAdditionalHint(cocktail, currentAttempt);
                        System.out.println();
                    }
                currentAttempt++;
            }
        }
        isGameOngoing = false;
        printEndGameStatistics(cocktailName);
        checkHighScore();
    }

    private void printAdditionalHint(Cocktail cocktail, int currentAttempt) {
        switch (currentAttempt) {
            case 1:
                System.out.println("Cocktail category:");
                System.out.println(cocktail.getCategory());
                break;
            case 2:
                System.out.println("Cocktail glass");
                System.out.println(cocktail.getGlass());
                break;
            case 3:
                System.out.println("Is cocktail alcoholic?");
                System.out.println(cocktail.getAlcoholic());
                break;
            case 4:
                System.out.println("All ingredients:");
                printAllIngredients(cocktail);
                break;
        }
        System.out.println();
    }

    private void printAllIngredients(Cocktail cocktail) {
        for (String s : cocktail.getIngredients()) {
            if (!s.equals("null")) {
                System.out.println(s);
            }
        }
    }

    private void printCocktailInstructions(Cocktail cocktail) {
        System.out.println("Guess the cocktail by its instructions:");
        System.out.println(cocktail.getInstructions());
    }

    private Cocktail getNewCocktail() {
        try {
            Cocktail cocktail = cocktailService.getUniqueCocktail(cocktailsBeenInTheGame);
            String cocktailName = cocktail.getName().toLowerCase();
            cocktailsBeenInTheGame.add(cocktailName);
            return cocktail;
        } catch (Exception e) {
            System.out.println("Error getiing a new unique cocktail: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public void printRoundIntro(String hiddenName, int currentAttempt) {
        System.out.println("Cocktail: " + hiddenName);
        System.out.println("Attempt nr. " + currentAttempt);
    }

    public String askForPlayerGuess() {
        System.out.print("Your guess: ");
        return scanner.nextLine().toLowerCase();
    }

    private String revealLetter(String hiddenName, String actualName, int numLettersToReveal) {
        StringBuilder revealed = new StringBuilder(hiddenName);
        List<Integer> unrevealedIndices = new ArrayList<>();

        for (int i = 0; i < actualName.length(); i++) {
            if (hiddenName.charAt(i) == '_' && Character.isLetter(actualName.charAt(i))) {
                unrevealedIndices.add(i);
            }
        }

       if(!isEnoughLettersToReveal(unrevealedIndices.size())) {
           return null;
       }

        Random random = new Random();
        for (int i = 0; i < numLettersToReveal; i++) {
            int randomIndex = random.nextInt(unrevealedIndices.size());
            int letterIndex = unrevealedIndices.get(randomIndex);

            revealed.setCharAt(letterIndex, actualName.charAt(letterIndex));
            unrevealedIndices.remove(randomIndex);
        }
        return revealed.toString();
    }

    private boolean isEnoughLettersToReveal(int unrevealedLetters) {
        return unrevealedLetters > gameDifficulty.getLettersToReveal();
    }

    private void printEndGameStatistics(String cocktailName) {
        System.out.println();
        System.out.println("You've used all your attempts. The cocktail was: " + cocktailName + ".");
        System.out.println("You've revealed " + revealedCocktails + " cocktails.");
        System.out.println("Your total Score: " + currentScore);
        System.out.println();
    }

    private void printCurrentScore() {
        System.out.println();
        System.out.println("Your current score: " + currentScore);
        System.out.println();
    }

    private void printHighScore() {
        System.out.println();
        System.out.println("Current high score: " + highScore);
        System.out.println();
    }

    private String getHiddenName(String name) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                output.append(" ");
            } else {
                output.append("_");
            }
        }
        return output.toString();
    }

    private void setGameDifficulty() {
        System.out.println("Choose difficulty level, how many letters will be revealed in case of wrong guess");
        System.out.println("Enter 3 for easy, 2 for normal and 1 for hard.");
        System.out.print("Your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 3:
                    gameDifficulty = GameDifficulty.EASY;
                    break;
                case 2:
                    gameDifficulty = GameDifficulty.NORMAL;
                    break;
                case 1:
                    gameDifficulty = GameDifficulty.HARD;
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to 2 - normal difficulty.");
                    gameDifficulty = GameDifficulty.NORMAL;
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Defaulting to 2 - normal difficulty.");
            scanner.nextLine();
            gameDifficulty = GameDifficulty.NORMAL;
        }
    }

    private void printRules() {
        System.out.println();
        System.out.println();
        System.out.println("********** ********** ********** ********** ********** ********** ********** ********** ********** ********** **********");
        System.out.println("  /$$$$$$                      /$$         /$$               /$$ /$$        /$$$$$$");
        System.out.println(" /$$__  $$                    | $$        | $$              |__/| $$       /$$__  $$");
        System.out.println("| $$  \\__/  /$$$$$$   /$$$$$$$| $$   /$$ /$$$$$$    /$$$$$$  /$$| $$      | $$  \\__/  /$$$$$$  /$$$$$$/$$$$   /$$$$$$");
        System.out.println("| $$       /$$__  $$ /$$_____/| $$  /$$/|_  $$_/   |____  $$| $$| $$      | $$ /$$$$ |____  $$| $$_  $$_  $$ /$$__  $$");
        System.out.println("| $$      | $$  \\ $$| $$      | $$$$$$/   | $$      /$$$$$$$| $$| $$      | $$|_  $$  /$$$$$$$| $$ \\ $$ \\ $$| $$$$$$$$");
        System.out.println("| $$    $$| $$  | $$| $$      | $$_  $$   | $$ /$$ /$$__  $$| $$| $$      | $$  \\ $$ /$$__  $$| $$ | $$ | $$| $$_____/");
        System.out.println("|  $$$$$$/|  $$$$$$/|  $$$$$$$| $$ \\  $$  |  $$$$/|  $$$$$$$| $$| $$      |  $$$$$$/|  $$$$$$$| $$ | $$ | $$|  $$$$$$$");
        System.out.println("\\______/  \\______/  \\_______/|__/  \\__/   \\___/   \\_______/|__/|__/       \\______/  \\_______/|__/ |__/ |__/ \\_______/");


        System.out.println();
        System.out.println("********** ********** ********** ********** ********** ********** ********** ********** ********** ********** **********");
        System.out.println("You must guess the name of the cocktail.");
        System.out.println("You have " + maxAttempts + " attempts to guess the name of the cocktail.");
        System.out.println("If answered correctly the game continues with a new random cocktail and score is increased by number of attempts left.");
        System.out.println("If your guess is wrong, some letter(s) and one hint will be revealed.");
        System.out.println("Your answers are case insensitive.");
        System.out.println("You can skip the round if you enter skip as a guess.");
        System.out.println("********** ********** ********** ********** ********** ********** ********** ********** ********** ********** **********");
        System.out.println();
    }


    public void setGameDifficulty(GameDifficulty gameDifficulty) {
    }
}
