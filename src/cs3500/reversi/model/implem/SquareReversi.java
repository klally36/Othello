package cs3500.reversi.model.implem;


import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;
import cs3500.reversi.model.interf.Player;


/**
 * Represents a standard square reversi game.
 */
public class SquareReversi extends AbstractReversi {

  /**
   * Constructs a HexagonReversi game with the given size, radius, and orientation.
   * 
   * @param size the size of the board (how many circles of cells in the board)
   * @param radius the radius of the cell in the board (distance from the center to the vertex)
   * @param orientation the orientation of the board in radians; counterclockwise from the positive
   *        x axis
   * @param blackPlayer the black player (Could be AI or human)
   * @param whitePlayer the white player (Could be AI or human)
   * @throws IllegalArgumentException if the size is not positive
   */
  public SquareReversi(int size, double radius, double orientation, Player blackPlayer,
      Player whitePlayer) {
    this.board = new SquareGameBoard(size, radius, orientation);

    playerB = blackPlayer;
    playerW = whitePlayer;

    this.gameState = GameStatus.BTURN;

    // Initialize the board
    this.board.getCell(0, 0).setCellStatus(CellStatus.BLACK);
    this.board.getCell(0, 1).setCellStatus(CellStatus.WHITE);
    this.board.getCell(0, 2).setCellStatus(CellStatus.BLACK);
    this.board.getCell(0, 3).setCellStatus(CellStatus.WHITE);

    this.refreshLegalMoves();
  }



}
