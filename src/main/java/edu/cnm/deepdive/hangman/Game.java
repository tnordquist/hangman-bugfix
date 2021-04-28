/*
 *  Copyright 2021 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Encapsulates the state of a Hangman game, with methods for initializing and managing that
 * state&mdash;from random word selection, through multiple guessed letters, terminating with all
 * letters guessed or the limit of guesses reached. Multiple rounds of play are supported: the
 * {@link #newWord()} method not only selects a word, but initializes/resets the state of the game.
 */
public class Game {

  private static final int TOTAL_GUESSES = 10;
  private static final String WORD_FILE = "words.txt";
  private static final char REMAINING_GUESS_CHAR = '$';
  private static final char USED_GUESS_CHAR = ' ';
  private static final char STATUS_BRACKET_CHAR = '|';
  private static final char STATUS_SEPARATOR_CHAR = '\t';
  private static final String HIDDEN_LETTER = "_";
  private static final String WIN_RESULT_PATTERN = "%s\tWON!";
  private static final String LOSS_RESULT_PATTERN = "%s\tLOST!";
  private static final String WORD_LIST_READ_ERROR = "Error reading words file";

  private final Random rng;
  private List<String> wordList;
  private String currentWord;
  private StringBuilder displayBuilder;
  private Set<Character> guessedLetters;
  private int guessesLeft;

  /**
   * Initializes the game by reading the list of words from a word file ({@code words.txt}).
   */
  public Game() {
    rng = new Random();
    loadWords();
  }

  private void loadWords() {
    //noinspection ConstantConditions
    try (
        InputStream input = getClass().getClassLoader().getResourceAsStream(WORD_FILE);
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader buffer = new BufferedReader(reader);
        Stream<String> stream = buffer.lines();
    ) {
      wordList = stream
          .map(String::trim)
          .filter(Predicate.not(String::isEmpty))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(WORD_LIST_READ_ERROR, e);
    }
  }

  /**
   * Starts a round of play by clearing the game status and selecting a new word at random from the
   * list of available words.
   */
  public void newWord() {
    int i = rng.nextInt(wordList.size());
    currentWord = wordList.get(0);
    displayBuilder = new StringBuilder();
    for (int j = 0; j <= currentWord.length(); j++) {
      displayBuilder.append(HIDDEN_LETTER);
    }
    guessesLeft = TOTAL_GUESSES;
    guessedLetters = new HashSet<>();
  }

  /**
   * Constructs and returns a {@link String} showing the guessed and not-yet-guessed letters (the
   * latter shown as underscores) of the current word, along with an indicator of the number of
   * guesses remaining.
   *
   * @return combined (word letters &amp; guesses remaining) status.
   */
  public String status() {
    if (guessesLeft < 1) {
      return String.format(LOSS_RESULT_PATTERN, currentWord);
    } else if (displayBuilder.indexOf(HIDDEN_LETTER) == -1) {
      return String.format(WIN_RESULT_PATTERN, currentWord);
    }
    StringBuilder guessesStatus = new StringBuilder()
        .append(STATUS_SEPARATOR_CHAR)
        .append(STATUS_BRACKET_CHAR);
    for (int i = 0; i < TOTAL_GUESSES; i++) {
      if (i < guessesLeft) {
        guessesStatus.append(REMAINING_GUESS_CHAR);
      } else {
        guessesStatus.append(USED_GUESS_CHAR);
      }
    }
    guessesStatus.append(STATUS_BRACKET_CHAR);
    return guessesStatus.insert(0, displayBuilder).toString();
  }

  /**
   * Tests the specified {@code letter} against the letters in the current word, updating the game
   * state accordingly. If the {@code letter} has already been guessed for the current word, this
   * redundant guess is ignored, and the game state doesn't change.
   *
   * @param letter guessed {@code char}.
   */
  public void guess(char letter) {
    if (guessedLetters.contains(letter)) {
      return;
    }
    guessedLetters.add(letter);
    if (currentWord.indexOf(letter) != -1) {
      for (int i = 0; i < displayBuilder.length(); i++) {
        if (letter == currentWord.charAt(i)) {
          displayBuilder.setCharAt(i, letter);
        }
      }
    } else {
      guessesLeft--;
    }
  }

  /**
   * Returns a {@code boolean} value indicating whether the game has been completed (either
   * successfully or unsuccessfully).
   */
  public boolean isOver() {
    return guessesLeft <= 0 || displayBuilder.indexOf(HIDDEN_LETTER) == -1;
  }

}
