package cs3500.reversi.model.interf;

import java.awt.Graphics;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;

/**
 * Represents a game board interface in the Reversi game.
 */
public interface Board {

  /**
   * Gets a cell at the specified coordinates. The Design of the coordinate may vary depending on
   * the implementation.
   * 
   * @param coordOne the first coordinate
   * @param coordTwo the second coordinate
   * @return the cell at the specified coordinates
   */
  Cell getCell(int coordOne, int coordTwo);

  /**
   * Gets a cell at the specified position.
   * 
   * @param position the position of the cell
   * @return the cell at the specified position; return null if the position is not inside the board
   * @throws IllegalArgumentException if the position is null
   */
  Cell getCell(Position position) throws IllegalArgumentException;


  /**
   * Gets the total number of coordinate one in the board.
   * 
   * @return the total number of coordinate one in the board
   */
  int getCoordOneSize();


  /**
   * Clone a deepcopy of the board.
   * @return a deepcopy of the board
   */
  Board cloneBoard();


  /**
   * Gets the total number of coordinate two in the first coordinate.
   * 
   * @param coordOne the first coordinate
   * @return the total number of coordinate two in the fisrt coordinate
   * @throws IllegalArgumentException if the first coordinate is out of range
   */
  int getCoordTwoSize(int coordOne) throws IllegalArgumentException;

  /**
   * Checks if two cells are on the same line in the grid.
   * 
   * @param gridOne the first cell
   * @param gridTwo the second cell
   * @return true if the two cells are on the same line in the grid, false otherwise
   * @throws IllegalArgumentException if either of the cells is null
   */
  boolean onSameLine(Cell gridOne, Cell gridTwo) throws IllegalArgumentException;


  /**
   * Checks if the given move is legal.
   * 
   * @param cell the cell to check
   * @param gameStatus the current game status
   * @return true if the given move is legal for this cell, false otherwise
   * @throws IllegalArgumentException if the cell is null
   */
  boolean isLegalMove(Cell cell, GameStatus gameStatus) throws IllegalArgumentException;

  /**
   * Makes a move on the board.
   * 
   * @param cell the cell to make the move
   * @param cellStatus the status of the cell
   * @throws IllegalArgumentException if the cell is not a legal move, or if the cell is null, or if
   *         the cell status is EMPTY
   */
  void oneMove(Cell cell, CellStatus cellStatus) throws IllegalArgumentException;

  /**
   * Paints the game board.
   * 
   * @param g the graphics
   * @param xCenter the x pixel coordinate of the center of the board
   * @param yCenter the y pixel coordinate of the center of the board
   */
  void paint(Graphics g, int xCenter, int yCenter) throws IllegalArgumentException;

  /**
   * Calculate the delta score difference after a move on the board.
   * 
   * @param cell       the cell to make the move
   * @throws IllegalArgumentException if the cell is not a legal move, or if the
   *                                  cell is null, or if the cell status is EMPTY
   */
  int getMoveScore(Cell cell, GameStatus gameStatus) throws IllegalArgumentException;
  
}
