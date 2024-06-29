package cs3500.reversi.model.implem;

import java.util.ArrayList;
import cs3500.reversi.model.position.CartesianPosition;
import cs3500.reversi.model.position.PolarPosition;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;
import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;


/**
 * Represents a polygon game board in the Reversi game with arbitrary number of sides.
 */
public class HexagonGameBoard extends AbstractPolygonGameBoard {

  private final int size;

  private final double radius;

  private final double orientation;

  /**
   * Constructs a polygon game board with the given parameters.
   *
   * @param size the size of the board (how many layers of cells)
   * @param radius the distance from the center of the cell to any vertice of the cell
   * @param orientation the orientation of the board in radians, counter-clockwise from the positive
   *        x-axis
   * @throws IllegalArgumentException if side is less than 3 or size is less than 2
   */
  public HexagonGameBoard(int size, double radius, double orientation)
      throws IllegalArgumentException {
    this.size = size;
    this.radius = radius;
    this.orientation = orientation;
    grid = new ArrayList<ArrayList<Cell>>();

    this.numOfSide = 6;

    // Initialize the grid
    double centerToCenterDist = 2 * radius * Math.cos(Math.PI / numOfSide);

    for (int i = 0; i < size; i++) {
      grid.add(new ArrayList<Cell>());

      // The center of the board at (0, 0)
      if (i == 0) {
        grid.get(i).add(new PolygonCell(new CartesianPosition(0, 0), radius, numOfSide,
                orientation));
      } else {
        for (int j = 0; j < 6 * i; j++) {
          if (j == 0) {
            Position cellCenter = new PolarPosition(i * centerToCenterDist, 0);
            grid.get(i).add(new PolygonCell(cellCenter, radius, numOfSide, orientation));
          } else {
            int directionIndex = Math.floorDiv(j - 1, i);
            Position cellCenter = grid.get(i).get(j - 1).getAdjacentCellCenter(directionIndex);
            grid.get(i).add(new PolygonCell(cellCenter, radius, numOfSide, orientation));
          }
        }
      }
    }
  }

  @Override
  public Board cloneBoard() {
    HexagonGameBoard clone = new HexagonGameBoard(size, radius, orientation);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < grid.get(i).size(); j++) {
        clone.grid.get(i).set(j, new PolygonCell(grid.get(i).get(j)));
      }
    }
    return clone;
  }

  @Override
  public boolean onSameLine(Cell gridOne, Cell gridTwo) {
    if (gridOne == null || gridTwo == null) {
      throw new IllegalArgumentException("gridOne and gridTwo cannot be null");
    }
    for (int i = 0; i < numOfSide; i++) {
      Cell adjacentCell = getAdjacentCell(gridOne, i);
      while (adjacentCell != null) {
        if (adjacentCell.equals(gridTwo)) {
          return true;
        }
        adjacentCell = getAdjacentCell(adjacentCell, i);
      }
    }
    return false;
  }

  @Override
  public boolean isLegalMove(Cell cell, GameStatus gameStatus) {
    switch (gameStatus) {
      case BTURN:
        return isLegalMoveForCellState(cell, CellStatus.BLACK);
      case WTURN:
        return isLegalMoveForCellState(cell, CellStatus.WHITE);
      default:
        return false;
    }
  }

  /**
   * Helper function to check if the given cell is a legal move for the given cell status.
   * 
   * @param cell the given cell
   * @param cellStatus the given cell status
   * @return true if the given cell is a legal move for the given cell status, false otherwise
   * @throws IllegalArgumentException if cell is null
   */
  private boolean isLegalMoveForCellState(Cell cell, CellStatus cellStatus)
      throws IllegalArgumentException {
    if (cell == null) {
      throw new IllegalArgumentException("cell cannot be null");
    }
    if (cell.getCellStatus() != CellStatus.EMPTY) {
      return false;
    }
    for (int i = 0; i < numOfSide; i++) {
      Cell adjacentCell = getAdjacentCell(cell, i);
      if (adjacentCell == null || adjacentCell.getCellStatus() == CellStatus.EMPTY
          || adjacentCell.getCellStatus() == cellStatus) {
        continue;
      }
      while (adjacentCell != null) {
        if (adjacentCell.getCellStatus() == cellStatus) {
          return true;
        } else if (adjacentCell.getCellStatus() == CellStatus.EMPTY) {
          break;
        } else {
          adjacentCell = getAdjacentCell(adjacentCell, i);
        }
      }
    }
    return false;
  }

  @Override
  public void oneMove(Cell cell, CellStatus cellStatus) {

    if (cell == null) {
      throw new IllegalArgumentException("cell cannot be null");
    }
    if (cellStatus == CellStatus.EMPTY) {
      throw new IllegalArgumentException("cellStatus cannot be EMPTY");
    }
    if (cell.getCellStatus() != CellStatus.EMPTY) {
      throw new IllegalArgumentException("cellStatus cannot be set to EMPTY");
    }
    cell.setCellStatus(cellStatus);

    for (int i = 0; i < numOfSide; i++) {
      ArrayList<Cell> cellsToFlip = new ArrayList<Cell>();
      Cell adjacentCell = getAdjacentCell(cell, i);

      if (adjacentCell == null || adjacentCell.getCellStatus() == CellStatus.EMPTY
          || adjacentCell.getCellStatus() == cellStatus) {
        continue;
      }
      while (true) {
        if (adjacentCell == null) {
          cellsToFlip = new ArrayList<Cell>();
          break;
        } else if (adjacentCell.getCellStatus() == cellStatus) {
          break;
        } else if (adjacentCell.getCellStatus() == CellStatus.EMPTY) {
          cellsToFlip = new ArrayList<Cell>();
          break;
        } else {
          cellsToFlip.add(adjacentCell);
          adjacentCell = getAdjacentCell(adjacentCell, i);
        }
      }
      for (Cell cellToFlip : cellsToFlip) {
        cellToFlip.flip();
      }
    }
  }
}
