function Hint({ cocktailHint, revealedHintsCount }) {
    if (!cocktailHint) {
      return null;
    }

    const hintsArray = [
        `Category: ${cocktailHint.category}`,
        `Glass: ${cocktailHint.glass}`,
        `Alcoholic: ${cocktailHint.alcoholic}`,
        `Ingredients: ${cocktailHint.ingredients.join(', ')}`,
      ];
  
    return (
      <div>
        <h3>Hints:</h3>
        <ul>
            {hintsArray.slice(0, revealedHintsCount).map((hint, index) => (
                <li key={index}>{hint}</li>
            ))}
        </ul>
      </div>
    );
  }
  
  export default Hint;
