package cs3500.reversi.model.implem;

import java.util.ArrayList;
import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.interf.Player;
import cs3500.reversi.model.status.GameStatus;

import java.util.Random;

/**
 * Represents a crazy AI player that randomly selects a legal move.
 */
public class CrazyAIPlayer implements Player {

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

    if (allLegalMoves.size() != 0) {
      Random rand = new Random();
      int randomIndex = rand.nextInt(allLegalMoves.size());
      return allLegalMoves.get(randomIndex);
    } 
    return null;
  }
}
