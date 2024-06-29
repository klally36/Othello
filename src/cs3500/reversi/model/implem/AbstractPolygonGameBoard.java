package cs3500.reversi.model.implem;

import java.awt.Graphics;
import java.util.ArrayList;

import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;


/**
 * AbstractPolygonGameBoard is an abstract class that represents a polygon game board in the
 * Reversi game with an arbitrary number of sides.
 * This class provides the basic functionality for a polygon game board, such as managing the grid
 * of cells and handling game moves.
 * Subclasses should implement the specific behavior for the type of polygon game board they
 * represent.
 */
public abstract class AbstractPolygonGameBoard implements Board {

  protected ArrayList<ArrayList<Cell>> grid;

  protected int numOfSide;

  @Override
  public int getCoordOneSize() {
    return this.grid.size();
  }

  @Override
  public void paint(Graphics g, int xCenter, int yCenter) {
    if (g == null) {
      throw new IllegalArgumentException("g cannot be null");
    }
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid.get(i).size(); j++) {
        grid.get(i).get(j).paint(g, xCenter, yCenter);
      }
    }
  }

  @Override
  public int getCoordTwoSize(int coordOne) {
    if (coordOne < 0 || coordOne >= this.grid.size()) {
      throw new IllegalArgumentException("circle must be between 0 and size - 1");
    }
    return grid.get(coordOne).size();
  }

  @Override
  public Cell getCell(int coordOne, int coordTwo) {
    if (coordOne < 0 || coordOne >= this.grid.size() || coordTwo < 0
        || coordTwo >= grid.get(coordOne).size()) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    return grid.get(coordOne).get(coordTwo);
  }

  @Override
  public Cell getCell(Position position) {
    if (position == null) {
      throw new IllegalArgumentException("position cannot be null");
    }
    for (int i = 0; i < this.grid.size(); i++) {
      for (int j = 0; j < grid.get(i).size(); j++) {
        if (grid.get(i).get(j).isPositionInside(position)) {
          return grid.get(i).get(j);
        }
      }
    }
    return null;
  }

  /**
   * Helper function to get the adjacent cell of the given cell in the given direction.
   * 
   * @param cell the given cell
   * @param directionIndex the direction of the adjacent cell (from 0 to side - 1)
   * @return the adjacent cell of the given cell in the given direction return null if the adjacent
   *         cell in that direction does not exist
   * @throws IllegalArgumentException if cell is null or directionIndex is not between 0 and side -
   *         1
   */
  protected Cell getAdjacentCell(Cell cell, int directionIndex) {
    if (directionIndex < 0 || directionIndex >= numOfSide) {
      throw new IllegalArgumentException("directionIndex must be between 0 and side - 1");
    }
    if (cell == null) {
      return null;
    }
    Position adjacentCellCenter = cell.getAdjacentCellCenter(directionIndex);
    return getCell(adjacentCellCenter);
  }

  private int countCellWithStatus(Board board, CellStatus cellStatus) {
    int count = 0;
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).getCellStatus() == cellStatus) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public int getMoveScore(Cell cell, GameStatus gameStatus) throws IllegalArgumentException {
    if (!isLegalMove(cell, gameStatus)) {
      throw new IllegalArgumentException("the move has to be valid!");
    }
    
    int currentScoreOnBoard;
    if (gameStatus == GameStatus.BTURN) {
      currentScoreOnBoard = countCellWithStatus(this, CellStatus.WHITE);
    } else if (gameStatus == GameStatus.WTURN) {
      currentScoreOnBoard = countCellWithStatus(this, CellStatus.BLACK);
    } else {
      throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
    }

    Board boardCopyForGreedy = this.cloneBoard();
    boardCopyForGreedy.oneMove(new PolygonCell(cell),
        gameStatus == GameStatus.BTURN ? CellStatus.BLACK : CellStatus.WHITE);

    int newScoreOnBoard;
    if (gameStatus == GameStatus.BTURN) {
      newScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.WHITE);
    } else if (gameStatus == GameStatus.WTURN) {
      newScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.BLACK);
    } else {
      throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
    }
    return (currentScoreOnBoard - newScoreOnBoard);

  }
}
