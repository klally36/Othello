package cs3500.reversi.model.interf;

import java.awt.Graphics;

/**
 * Represents the read-only model of the Reversi game for the View.
 */
public interface ReadonlyReversiModel {

  /**
   *  Paints the game board.
   * 
   * @param g the graphics object
   * @param xCenter the x coordinate of the center of the board
   * @param yCenter the y coordinate of the center of the board
   *
   */
  void paint(Graphics g, int xCenter, int yCenter) throws IllegalArgumentException;
  
}
