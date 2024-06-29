package cs3500.reversi.model.interf;

import cs3500.reversi.model.status.GameStatus;

/**
 * Represents the model of the Reversi game.
 */
public interface ReversiModel {

  /**
   * Gets the game board.
   * 
   * @return the game board
   */
  Board getBoard();

  /**
   * Gets the current game state.
   * 
   * @return the current game state
   */
  GameStatus getGameState();

  /**
   * Refreshes the legal move status of all the cells on the board.
   */
  void refreshLegalMoves();

  /**
   * Update to the current game play, and execute the following actions sequentially. 1) If the game
   * is over, set the game state and find the winner of the game. 2) If the game is not over, - If
   * the current player is an AI, make a move. - If the current player is a human, make a move.
   * 
   */
  void update();

  /**
   * Check if there is no legal move for the both player on the board.
   * 
   * @return true if there is no legal move for the both player on the board, false otherwise
   */
  boolean isGameOver();

  /**
   * Passes the turn to the other player.
   */
  void pass();

}
