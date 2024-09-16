import React, { useState, useEffect } from 'react';
import axios from 'axios';
import GuessForm from './GuessForm';
import Hint from './Hint';
import Score from './Score';
import DifficultySelector from './DifficultySelector';

function Game() {
  const [gameState, setGameState] = useState(null);
  const [rules, setRules] = useState('');
  const [gameStarted, setGameStarted] = useState(false);

  useEffect(() => {
    fetchRules();
  }, []);

  const fetchRules = async () => {
    try {
      const response = await axios.get('/api/cocktailgame/rules');
      setRules(response.data);
    } catch (error) {
      console.error('Error fetching rules:', error);
    }
  };

  const startGame = async (selectedDifficulty) => {
    try {
      const response = await axios.post('/api/cocktailgame/start', {
        difficulty: selectedDifficulty,

      });
      setGameState(response.data);
      setGameStarted(true);
    } catch (error) {
      console.error('Error starting game:', error);
    }
  };

  const makeGuess = async (guess) => {
    try {
      const response = await axios.post(`/api/cocktailgame/${gameState.gameId}/guess`, {
        guess: guess,
      });
      const updatedGameState = response.data;

      if (updatedGameState.newHighScore) {
        alert('Congratulations! You achieved a new high score!');
      }

      if (updatedGameState.gameOver === true) {
        alert(updatedGameState.gameOverMessage);
        setGameStarted(false);
        setGameState(null); 
      } else {
        setGameState(updatedGameState);
      }
    } catch (error) {
      console.error('Error making guess:', error);
    }
  };

  const skipRound = async () => {
    try {
      const response = await axios.post(`/api/cocktailgame/${gameState.gameId}/skip`);
      setGameState(response.data);
    } catch (error) {
      console.error('Error skipping round:', error);
    }
  };

  const handleSelectDifficulty = (selectedDifficulty) => {
    startGame(selectedDifficulty);
  };

  return (
    <div>
      <h2>Game Rules</h2>
      <p>{rules.split('\n')[0]}</p>
      <ul>
        {rules.split('\n').slice(1).map((rule, index) => (
          <li key={index}>{rule}</li>
        ))}
      </ul>

      {!gameStarted && (
        <div>
          <DifficultySelector onSelectDifficulty={handleSelectDifficulty} />
        </div>
      )}

      {gameStarted && gameState && (
        <div>
          <Score currentScore={gameState.currentScore} highScore={gameState.highScore} />
          <p>{gameState.instructions}</p>
          <p>{gameState.hiddenName}</p>
          <Hint cocktailHint={gameState.cocktailHint} revealedHintsCount={gameState.revealedHintsCount} />          
          <GuessForm onSubmit={makeGuess} />
          <button onClick={skipRound}>Skip Round</button>
        </div>
      )}

      {gameStarted && !gameState && <div>Loading...</div>}
    </div>
  );
}

export default Game;