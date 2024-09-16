import React, { useState } from 'react';

function GuessForm({ onSubmit }) {
  const [guess, setGuess] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit(guess);
    setGuess('');
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={guess}
        onChange={(event) => setGuess(event.target.value)}
        placeholder="Enter your guess"
      />
      <button type="submit">Submit</button>
    </form>
  );
}

export default GuessForm;
