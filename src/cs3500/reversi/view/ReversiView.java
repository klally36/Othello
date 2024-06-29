package cs3500.reversi.view;

import java.awt.Graphics;


/**
 * Represents a visual view of the Reversi game.
 */
public interface ReversiView {

  /**
   * Paints the game board.
   * 
   * @param g the graphics object
   * @throws IllegalArgumentException if the graphics object is null
   */
  void paint(Graphics g) throws IllegalArgumentException;
  
}
