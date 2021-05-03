package edu.cnm.deepdive.hangman;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

class GameTest {

  @Test
  void constructor() {
    Game game = new Game();
    assertTrue(game.getWordList() != null && !game.getWordList().isEmpty());
  }

  @Test
  void newWord() {
    Game game = new Game();
    game.newWord();
    assertAll(() -> assertTrue(game.getGuessesLeft() > 0),
        () -> assertTrue(game.getWordList().contains(game.getCurrentWord())),
        () -> assertTrue(game.getGuessedLetters().isEmpty()),
        () -> assertEquals(game.getCurrentWord().length(), game.getDisplayBuilder().length()),
        () -> assertTrue(game.getDisplayBuilder().toString().matches("^_+$")));
  }

  @Test
  void guess() {
    fail("Not yet implemented");
  }

  @Test
  void isOver() {
    fail("Not yet implemented");
  }

}
