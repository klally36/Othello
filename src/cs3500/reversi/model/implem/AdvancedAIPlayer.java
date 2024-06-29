package cs3500.reversi.model.implem;

import java.util.ArrayList;
import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.interf.Player;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;


/**
 * Represents an advanced AI player that implements a minimax strategy with other preferences.
 */
public class AdvancedAIPlayer implements Player {

  // Preference Parameters for the
  private final int cornerTendency;
  private final int nextToCornerPhobia;
  private final int greedy;
  private final int cautiousness;
  private final int numOfSide;

  /**
   * Constructs an AIPlayer with the given preference parameters.
   * 
   * @param cornerTendency the tendency to place a piece on a corner
   * @param nextToCornerPhobia the phobia of placing a piece next to a corner
   * @param greedy the tendency to place a piece on a cell that will flip the most pieces
   * @param cautiousness the tendency to place a piece on a cell that not give the opponent a chance
   *        to flip the most pieces
   */
  public AdvancedAIPlayer(int numOfSide, int cornerTendency, int nextToCornerPhobia, int greedy,
      int cautiousness) {
    this.cornerTendency = cornerTendency;
    this.nextToCornerPhobia = nextToCornerPhobia;
    this.greedy = greedy;
    this.cautiousness = cautiousness;
    this.numOfSide = numOfSide;
  }

  /**
   * Helper method to determine if the given cell is a corner cell.
   * 
   * @param coordOne the first coordinate of the cell
   * @param coordTwo the second coordinate of the cell
   * @param board the board
   * @return true if the given cell is a corner cell, false otherwise
   */
  private boolean isCorner(Cell cell, Board board) {
    int noCells = 0;
    for (int i = 0; i < numOfSide; i++) {
      if (board.getCell(cell.getAdjacentCellCenter(i)) == null) {
        noCells++;
      }
    }
    return noCells == (int) (numOfSide / 2);
  }

  /**
   * Helper method to determine if the given cell is next to a corner cell.
   * 
   * @param coordOne the first coordinate of the cell
   * @param coordTwo the second coordinate of the cell
   * @param board the board
   * @return true if the given cell is next to a corner cell, false otherwise
   */
  private boolean isNextToCorner(Cell cell, Board board) {
    ArrayList<Cell> adjacentCells = new ArrayList<Cell>();
    for (int i = 0; i < numOfSide; i++) {
      if (board.getCell(cell.getAdjacentCellCenter(i)) != null) {
        adjacentCells.add(board.getCell(cell.getAdjacentCellCenter(i)));
      }
    }

    for (int i = 0; i < adjacentCells.size(); i++) {
      if (isCorner(adjacentCells.get(i), board)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Helper method to count the number of cells with the given status.
   * 
   * @param board the board
   * @param cellStatus the status of the cells
   * @return the number of cells with the given status
   */
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

  private int getScore(Cell cell, Board board, GameStatus gameStatus) {
    int score = 0;

    // Corner Tendency
    if (isCorner(cell, board)) {
      score += cornerTendency;
    }

    // Next to Corner Phobia
    if (isNextToCorner(cell, board)) {
      score -= nextToCornerPhobia;
    }

    GameStatus hypotheticalGameStatus = gameStatus;
    // Greedy (Get the most pieces flipped)
    int currentScoreOnBoard;
    if (hypotheticalGameStatus == GameStatus.BTURN) {
      currentScoreOnBoard = countCellWithStatus(board, CellStatus.BLACK);
    } else if (hypotheticalGameStatus == GameStatus.WTURN) {
      currentScoreOnBoard = countCellWithStatus(board, CellStatus.WHITE);
    } else {
      throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
    }

    Board boardCopyForGreedy = board.cloneBoard();
    boardCopyForGreedy.oneMove(new PolygonCell(cell),
        hypotheticalGameStatus == GameStatus.BTURN ? CellStatus.BLACK : CellStatus.WHITE);

    int newScoreOnBoard;
    if (hypotheticalGameStatus == GameStatus.BTURN) {
      newScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.BLACK);
    } else if (hypotheticalGameStatus == GameStatus.WTURN) {
      newScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.WHITE);
    } else {
      throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
    }

    score += (newScoreOnBoard - currentScoreOnBoard) * greedy;

    // Cautiousness (Don't give the opponent a chance to flip the most pieces)

    if (hypotheticalGameStatus == GameStatus.BTURN) {
      hypotheticalGameStatus = GameStatus.WTURN;
    } else if (hypotheticalGameStatus == GameStatus.WTURN) {
      hypotheticalGameStatus = GameStatus.BTURN;
    }

    int opponentScoreOnBoard;
    if (hypotheticalGameStatus == GameStatus.WTURN) {
      opponentScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.WHITE);
    } else if (hypotheticalGameStatus == GameStatus.BTURN) {
      opponentScoreOnBoard = countCellWithStatus(boardCopyForGreedy, CellStatus.BLACK);
    } else {
      throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
    }

    // refresh legal moves for the opponent
    for (int i = 0; i < boardCopyForGreedy.getCoordOneSize(); i++) {
      for (int j = 0; j < boardCopyForGreedy.getCoordTwoSize(i); j++) {
        if (boardCopyForGreedy.isLegalMove(boardCopyForGreedy.getCell(i, j),
            hypotheticalGameStatus)) {
          boardCopyForGreedy.getCell(i, j).setLegalMove(true);
        } else {
          boardCopyForGreedy.getCell(i, j).setLegalMove(false);
        }
      }
    }

    ArrayList<Cell> opponentLegalMoves = new ArrayList<Cell>();
    for (int i = 0; i < boardCopyForGreedy.getCoordOneSize(); i++) {
      for (int j = 0; j < boardCopyForGreedy.getCoordTwoSize(i); j++) {
        if (boardCopyForGreedy.getCell(i, j).isLegalMove()) {
          opponentLegalMoves.add(boardCopyForGreedy.getCell(i, j));
        }
      }
    }

    ArrayList<Integer> opponentScores = new ArrayList<Integer>();
    for (int i = 0; i < opponentLegalMoves.size(); i++) {
      Board boardCopyForOpponent = boardCopyForGreedy.cloneBoard();
      boardCopyForOpponent.oneMove(new PolygonCell(opponentLegalMoves.get(i)),
          hypotheticalGameStatus == GameStatus.BTURN ? CellStatus.BLACK : CellStatus.WHITE);
      int opponentNewScoreOnBoard;
      if (hypotheticalGameStatus == GameStatus.WTURN) {
        opponentNewScoreOnBoard = countCellWithStatus(boardCopyForOpponent, CellStatus.WHITE);
      } else if (hypotheticalGameStatus == GameStatus.BTURN) {
        opponentNewScoreOnBoard = countCellWithStatus(boardCopyForOpponent, CellStatus.BLACK);
      } else {
        throw new IllegalArgumentException("gameStatus cannot be GAMEOVER");
      }
      opponentScores.add(opponentNewScoreOnBoard - opponentScoreOnBoard);
    }

    int maxOpponentScore = 0;
    for (int i = 0; i < opponentScores.size(); i++) {
      if (opponentScores.get(i) > maxOpponentScore) {
        maxOpponentScore = opponentScores.get(i);
      }
    }

    score -= maxOpponentScore * cautiousness;
    return score;
  }

  @Override
  public Cell move(Board board, GameStatus gameStatus) {
    if (board == null) {
      throw new IllegalArgumentException("board cannot be null");
    }

    ArrayList<Cell> allLegalMoves = new ArrayList<Cell>();
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).isLegalMove()) {
          allLegalMoves.add(board.getCell(i, j));
        }
      }
    }

    if (allLegalMoves.size() == 0) {
      return null;
    }

    ArrayList<Integer> scores = new ArrayList<Integer>();
    for (int i = 0; i < allLegalMoves.size(); i++) {
      scores.add(getScore(allLegalMoves.get(i), board, gameStatus));
    }

    int maxScoreIndex = 0;
    for (int i = 0; i < scores.size(); i++) {
      if (scores.get(i) > scores.get(maxScoreIndex)) {
        maxScoreIndex = i;
      }
    }
    System.out.println("maxScoreIndex: " + maxScoreIndex);
    return allLegalMoves.get(maxScoreIndex);
  }
}
