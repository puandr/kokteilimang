import React, { useState } from 'react';

function DifficultySelector({ onSelectDifficulty }) {
  const [selectedDifficulty, setSelectedDifficulty] = useState('NORMAL');

  const handleSelectChange = (event) => {
    setSelectedDifficulty(event.target.value);
  };

  const handleStartGame = () => {
    onSelectDifficulty(selectedDifficulty);
  };

  return (
    <div>
      <h3>Select Difficulty:</h3>
      <p>Easy for revealing 3 letters in case of wrong answer</p>
      <p>Normal for revealing 2 letters in case of wrong answer</p>
      <p>Hard for revealing 1 letter in case of wrong answer</p>

      <select value={selectedDifficulty} onChange={handleSelectChange}>
        <option value="EASY">Easy</option>
        <option value="NORMAL">Normal</option>
        <option value="HARD">Hard</option>
      </select>
      <button onClick={handleStartGame}>Start Game</button>
    </div>
  );
}

export default DifficultySelector;