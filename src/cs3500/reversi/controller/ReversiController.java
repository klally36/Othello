package cs3500.reversi.controller;

import cs3500.reversi.model.interf.ReversiModel;
import cs3500.reversi.model.position.Position;


/**
 * Represents a controller interface in the Reversi game.
 */
public interface ReversiController {

  /**
   * Plays the game.
   * 
   * @param model the model of the game
   * @param windowWidth the width of the window
   * @param windowHeight the height of the window
   */
  void playGame(ReversiModel model, int windowWidth, int windowHeight);


  /**
   * Handles the key input. only pass legal key code ("P" and "Enter") to the model; will not react
   * to other key codes.
   * 
   * @param keyCode the key code
   */
  void handleKeyInput(int keyCode);


  /**
   * Handles the mouse input. When the mouse position will be mapped to the board position and
   * passed to the model.
   * 
   * @param p the position of the mouse
   */
  void handleMouseInput(Position p);
}
