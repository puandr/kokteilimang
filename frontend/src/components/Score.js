import React from 'react';

function Score({ currentScore, highScore }) {
  return (
    <div>
      <p>Current Score: {currentScore}</p>
      <p>High Score: {highScore}</p>
    </div>
  );
}

export default Score;
