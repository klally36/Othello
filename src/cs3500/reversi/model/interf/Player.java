package cs3500.reversi.model.interf;

import cs3500.reversi.model.status.GameStatus;


/**
 * Represents a player. (Could be human or AI)
 */
public interface Player {

  /**
   * Returns the cell that the player wants to place their piece on.
   * 
   * @param board the board that the player is playing on
   * @param gameStatus the status of the game
   * @return the cell that the player wants to place their piece on; return null if the player
   *         cannot place a piece or if the player is human, which inputs by clicking
   * 
   * @throws IllegalArgumentException if the board is null
   * @throws IllegalStateException if the state of the board is not valid. (e.g. more than one cell
   *         is selected)
   */
  Cell move(Board board, GameStatus gameStatus) throws IllegalArgumentException;
}
