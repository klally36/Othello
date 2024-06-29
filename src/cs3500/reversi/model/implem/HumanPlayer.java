package cs3500.reversi.model.implem;

import java.util.ArrayList;
import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.interf.Player;
import cs3500.reversi.model.status.GameStatus;



/**
 * Represents a human player.
 */
public class HumanPlayer implements Player {

  @Override
  public Cell move(Board board, GameStatus gameStatus) {
    if (board == null) {
      throw new IllegalArgumentException("board cannot be null");
    }

    ArrayList<Cell> selectedCells = new ArrayList<Cell>();

    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).isSelected()) {
          selectedCells.add(board.getCell(i, j));
        }
      }
    }

    if (selectedCells.size() == 1) {
      return selectedCells.get(0);
    } else if (selectedCells.size() > 1) {
      throw new IllegalStateException("More than one cell is selected");
    }
    return null;
  }
}
