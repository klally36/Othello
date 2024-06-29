package cs3500.reversi.model.interf;

import java.awt.Graphics;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;

/**
 * Represents a cell in the Reversi game.
 */
public interface Cell {

  /**
   * Checks if a position is inside the cell.
   * 
   * @param targetPosition the position to check
   * @return true if the position is inside the cell, false otherwise
   * @throws IllegalArgumentException if targetPosition is null
   */
  boolean isPositionInside(Position targetPosition) throws IllegalArgumentException;

  /**
   * Gets the status of the cell.
   * 
   * @return the cell tatus of the cell
   */
  CellStatus getCellStatus();

  /**
   * Sets the status of the cell.
   * 
   * @param cellStatus the status of the cell
   * @throws IllegalArgumentException if cellStatus is set to EMPTY (which violates a invariant)
   */
  void setCellStatus(CellStatus cellStatus) throws IllegalArgumentException;

  /**
   * Gets the position of the cell.
   * 
   * @return the position of the cell
   */
  Position getPosition();

  /**
   * Gets the center of an adjacent cell.
   * 
   * @param index the index of the adjacent cell (0 is the cell at the right of the first vertex),
   *        ranging from 0 to sides - 1
   * @return the center of the adjacent cell
   * @throws IllegalArgumentException if index is out of range
   */
  Position getAdjacentCellCenter(int index) throws IllegalArgumentException;

  /**
   * Flips the cell. This is only valid if the cell is not empty.
   * This method will change the status of the cell to the opposite of its current status
   * (BLACK -> WHITE, WHITE -> BLACK).
   * 
   * @throws IllegalArgumentException if the cell is empty (cannot be flipped)
   */
  void flip() throws IllegalArgumentException;

  /**
   * Checks if the cell is a legal move.
   * 
   * @return true if the cell is a legal move for current turn, false otherwise
   */
  boolean isLegalMove();

  /**
   * Sets if the cell is a legal move.
   * 
   * @param legalMove true if the cell is a legal move for current turn, false otherwise
   */
  void setLegalMove(boolean legalMove);


  /**
   * Checks if the cell is selected.
   * 
   * @return true if the cell is selected, false otherwise
   */
  boolean isSelected();


  /**
   * Select the cell.
   */
  void select();

  /**
   * Deselect the cell.
   */
  void deselect();

  /**
   * Paints the cell.
   * 
   * @param g the Graphics object to paint on
   * @param xCenter the x pixel coordinate of the center of the cell
   * @param yCenter the y pixel coordinate of the center of the cell
   * @throws IllegalArgumentException if g is null
   */
  void paint(Graphics g, int xCenter, int yCenter) throws IllegalArgumentException;

}
