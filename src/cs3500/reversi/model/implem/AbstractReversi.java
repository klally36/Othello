package cs3500.reversi.model.implem;

import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.Cell;
import cs3500.reversi.model.interf.Player;
import cs3500.reversi.model.interf.ReadonlyReversiModel;
import cs3500.reversi.model.interf.ReversiModel;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.status.GameStatus;
import java.awt.Color;
import java.awt.Graphics;


/**
 * This class provides an abstract implementation of a Reversi game model.
 * It includes common functionalities for both square and hexagonal Reversi games.
 */
public abstract class AbstractReversi implements ReversiModel, ReadonlyReversiModel {

  protected Board board;

  protected GameStatus gameState;

  protected Player playerB;
  protected Player playerW;

  /**
   * make a move on the given cell on the board, and deselect all the cells afterward.
   * 
   * @param cell the cell to make a move on
   */
  private void makeOneMove(Cell cell) {
    if (cell == null) {
      throw new IllegalArgumentException("cell cannot be null");
    }
    if (gameState == GameStatus.BTURN) {
      if (cell.isLegalMove()) {
        board.oneMove(cell, CellStatus.BLACK);
        this.switchTurn();
        this.refreshLegalMoves();
      }
    } else if (gameState == GameStatus.WTURN) {
      if (cell.isLegalMove()) {
        board.oneMove(cell, CellStatus.WHITE);
        this.switchTurn();
        this.refreshLegalMoves();
      }
    }
    // Deselect all cells after a move
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        board.getCell(i, j).deselect();
      }
    }
  }

  @Override
  public boolean isGameOver() {
    int currentMoveCount = 0;
    int alternativeLegalMoveCount = 0;
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).isLegalMove()) {
          currentMoveCount++;
        }
      }
    }
    switchTurn();
    refreshLegalMoves();
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).isLegalMove()) {
          alternativeLegalMoveCount++;
        }
      }
    }
    switchTurn();
    refreshLegalMoves();
    return currentMoveCount == 0 && alternativeLegalMoveCount == 0;
  }

  /**
   * Helper function to count the number of cells with the given status.
   * 
   * @param cellStatus the status of the cell
   * @return the number of cells with the given status
   */
  private int countCellWithStatus(CellStatus cellStatus) {
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
  public void pass() {
    if (gameState == GameStatus.BTURN) {
      this.switchTurn();
      this.refreshLegalMoves();
      this.update();
    } else if (gameState == GameStatus.WTURN) {
      this.switchTurn();
      this.refreshLegalMoves();
      this.update();
    }
  }

  @Override
  public void update() {

    if (this.isGameOver()) {
      int blackCount = this.countCellWithStatus(CellStatus.BLACK);
      int whiteCount = this.countCellWithStatus(CellStatus.WHITE);
      if (blackCount > whiteCount) {
        gameState = GameStatus.BWINS;
      } else if (blackCount < whiteCount) {
        gameState = GameStatus.WWINS;
      } else {
        gameState = GameStatus.DRAW;
      }
    }

    Cell move;
    switch (gameState) {
      case BTURN:
        move = playerB.move(board, gameState);
        if (move == null) {
          break;
        } else {
          makeOneMove(move);
          this.update();
        }
        break;
      case WTURN:
        move = playerW.move(board, gameState);
        if (move == null) {
          break;
        } else {
          makeOneMove(move);
          this.update();
        }
        break;
      default:
        break;
    }
  }


  /**
   * Helper function to switch the turn.
   */
  private void switchTurn() {
    if (gameState == GameStatus.BTURN) {
      gameState = GameStatus.WTURN;
    } else if (gameState == GameStatus.WTURN) {
      gameState = GameStatus.BTURN;
    }
  }

  @Override
  public Board getBoard() {
    return this.board;
  }

  @Override
  public GameStatus getGameState() {
    return this.gameState;
  }

  @Override
  public void refreshLegalMoves() {
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.isLegalMove(board.getCell(i, j), gameState)) {
          board.getCell(i, j).setLegalMove(true);
        } else {
          board.getCell(i, j).setLegalMove(false);
        }
      }
    }
  }

  @Override
  public void paint(Graphics g, int xCenter, int yCenter) {
    g.setColor(Color.BLACK);
    g.setFont(g.getFont().deriveFont(24.0f));
    g.drawString("Turn: ", 2 * xCenter - 125, 2 * yCenter - 20);
    if (gameState == GameStatus.BTURN) {
      g.setColor(Color.BLACK);
    } else if (gameState == GameStatus.WTURN) {
      g.setColor(Color.WHITE);
    } else {
      g.setColor(Color.DARK_GRAY);
    }
    g.fillOval(2 * xCenter - 55, 2 * yCenter - 40, 30, 30);
    board.paint(g, xCenter, yCenter);

    int moveScore = 0;
    int xCoord = xCenter;
    int yCoord = yCenter;

    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        if (board.getCell(i, j).isSelected()) {
          moveScore = board.getMoveScore(board.getCell(i, j), this.gameState);
          xCoord = (int) board.getCell(i, j).getPosition().getPixelX(xCenter);
          yCoord = (int) board.getCell(i, j).getPosition().getPixelY(yCenter);
          break;
        }
      }
    }

    if (moveScore != 0) {
      g.setFont(g.getFont().deriveFont(24.0f));
      g.setColor(Color.BLACK);
      g.drawString("" + moveScore, xCoord, yCoord);
    }

    int blackScore = this.countCellWithStatus(CellStatus.BLACK);
    int whiteScore = this.countCellWithStatus(CellStatus.WHITE);

    g.setFont(g.getFont().deriveFont(24.0f));
    g.setColor(Color.BLACK);
    g.drawString("Black: " + blackScore, 2 * xCenter - 125, 2 * yCenter - 120);
    g.drawString("White: " + whiteScore, 2 * xCenter - 125, 2 * yCenter - 70);

    if (this.isGameOver()) {
      g.setFont(g.getFont().deriveFont(32.0f));
      if (gameState == GameStatus.BWINS) {
        g.setColor(Color.BLACK);
        g.drawString("Black Wins!", xCenter - 100, 2 * yCenter - 40);
      } else if (gameState == GameStatus.WWINS) {
        g.setColor(Color.WHITE);
        g.drawString("White Wins!", xCenter - 100, 2 * yCenter - 40);
      } else {
        g.setColor(Color.RED);
        g.drawString("Draw!", xCenter - 100, 2 * yCenter - 40);
      }
    }
  }
  
}
