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

import java.util.Scanner;

/**
 * Entry point of a console-mode Hangman game program. Diagnosis and correction of issues with this
 * program form a problem in the practical exam materials of the Deep Dive Coding Java training
 * programs.
 */
public class Main {

  private static final String BLANK_GUESS_MESSAGE = "Sorry, couldn't read that";
  private static final String INVALID_CHARACTER_MESSAGE = "Not a letter";

  /**
   * Creates an instance of {@link Game}, then invokes its methods repeatedly to play an indefinite
   * number of rounds. Each round starts with the selection of a new word, followed by reading
   * the user's letter guesses until the word is completely revealed, or until the number of guesses
   * exceeds the limit.
   *
   * @param args Command-line arguments (currently ignored).
   */
  public static void main(String[] args) {
    Game game = new Game();
    Scanner s = new Scanner(System.in);
    while (true) {
      game.newWord();
      while (!game.isOver()) {
        System.out.println(game.status());
        String str = s.nextLine().toLowerCase().trim();
        if (str.isEmpty()) {
          System.out.println(BLANK_GUESS_MESSAGE);
        }; else if (Character.isLetter(str.charAt(0))) {
          game.guess(str.charAt(0));
        } else {
          System.out.println(INVALID_CHARACTER_MESSAGE);
        }
      }
      System.out.println(game.status());
    }
  }

}
