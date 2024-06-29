package cs3500.reversi.model.implem;

import java.util.ArrayList;

import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.position.CartesianPosition;
import cs3500.reversi.model.position.PolarPosition;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;

/**
 * This class represents a game board for a square Reversi game.
 * It extends the AbstractPolygonGameBoard class and provides the specifics for a square game board.
 */
public class SquareGameBoard extends AbstractPolygonGameBoard {

  private final int size;

  private final double radius;

  private final double orientation;

  /**
   * Constructs a square game board with the given parameters.
   * 
   * @param size the size of the board (how many layers of cells)
   * @param radius the distance from the center of the cell to any vertice of the cell
   * @param orientation the orientation of the board in radians, counter-clockwise from the positive
   *        x-axis
   * @throws IllegalArgumentException if side is less than 3 or size is less than 2
   */
  public SquareGameBoard(int size, double radius, double orientation)
      throws IllegalArgumentException {
    this.size = size;
    this.radius = radius;
    this.orientation = orientation;
    grid = new ArrayList<ArrayList<Cell>>();

    this.numOfSide = 4;

    // Initialize the grid
    for (int i = 0; i < size; i++) {
      grid.add(new ArrayList<Cell>());

      if (i == 0) {
        for (int j = 0; j < 4; j++) {
          Position center = new CartesianPosition(0, 0).add(new PolarPosition(radius,
                  Math.PI / 4 + j * Math.PI / 2));
          grid.get(i).add(new PolygonCell(center, radius, numOfSide, orientation));
        }
      }
      else {
        for (int j = 0; j < 4 + 8 * i; j++) {
          if (j == 0) {
            Position center =
                    grid.get(i - 1).get(grid.get(i - 1).size() - 1).getAdjacentCellCenter(3);
            grid.get(i).add(new PolygonCell(center, radius, numOfSide, orientation));
          } else {
            int directionIndex = Math.floorDiv(j, 2 * i + 1);
            Position center = grid.get(i).get(j - 1).getAdjacentCellCenter(directionIndex);
            grid.get(i).add(new PolygonCell(center, radius, numOfSide, orientation));
          }
        }
      }
    }
  }

  @Override
  public Board cloneBoard() {
    SquareGameBoard clone = new SquareGameBoard(size, radius, orientation);
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

    // Diagonal line
    for (int i = 0; i < numOfSide; i++) {
      Cell adjacentCell = getAdjacentCell(gridOne, i);
      adjacentCell = getAdjacentCell(adjacentCell, (i + 1) % numOfSide);
      while (adjacentCell != null) {
        if (adjacentCell.equals(gridTwo)) {
          return true;
        }
        adjacentCell = getAdjacentCell(adjacentCell, i);
      }
    }
    return false;
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

    for (int i = 0; i < numOfSide; i++) {
      Cell adjacentCell = getAdjacentCell(cell, i);
      adjacentCell = getAdjacentCell(adjacentCell, (i + 1) % 4);

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
          adjacentCell = getAdjacentCell(adjacentCell, (i + 1) % 4);
        }
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

    for (int i = 0; i < numOfSide; i++) {
      ArrayList<Cell> cellsToFlip = new ArrayList<Cell>();
      Cell adjacentCell = getAdjacentCell(cell, i);
      adjacentCell = getAdjacentCell(adjacentCell, (i + 1) % numOfSide);
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
          adjacentCell = getAdjacentCell(adjacentCell, (i + 1) % numOfSide);
        }
      }
      for (Cell cellToFlip : cellsToFlip) {
        cellToFlip.flip();
      }
    }
  }
}
