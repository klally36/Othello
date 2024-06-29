package cs3500.reversi.controller;

import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import cs3500.reversi.model.interf.Board;
import cs3500.reversi.model.interf.ReadonlyReversiModel;
import cs3500.reversi.model.interf.ReversiModel;
import cs3500.reversi.view.ReversiVisualUserInterface;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.interf.Cell;

/**
 * The ReversiVisualController class is an implementation of the ReversiController interface. It
 * handles user inputs and controls the flow of the game in a visual context.
 */
public class ReversiVisualController implements ReversiController {

  private JPanel view;
  private ReversiModel model;

  /**
   * Starts the game with the given model and window dimensions.
   *
   * @param model the game model
   * @param windowWidth the width of the game window
   * @param windowHeight the height of the game window
   */
  public void playGame(ReversiModel model, int windowWidth, int windowHeight) {
    JFrame frame = new JFrame("Reversi");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    this.model = model;
    this.view = new ReversiVisualUserInterface((ReadonlyReversiModel)model, this,
            windowWidth, windowHeight);
    frame.getContentPane().add(view);
    frame.addKeyListener((KeyListener) view);
    frame.pack();
    frame.setVisible(true);
  }

  private void resetSelections() {
    Board board = model.getBoard();
    Cell currentCell;
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {
        currentCell = board.getCell(i, j);
        currentCell.deselect();
      }
    }
  }

  @Override
  public void handleKeyInput(int keyCode) {

    if (keyCode == 80 && !model.isGameOver()) {
      resetSelections();
      model.pass();
      view.repaint();
    } else if (keyCode == 10 && !model.isGameOver()) {
      model.update();
      view.repaint();
    }
  }

  @Override
  public void handleMouseInput(Position p) {

    Cell currentCell;
    Board board = model.getBoard();
    for (int i = 0; i < board.getCoordOneSize(); i++) {
      for (int j = 0; j < board.getCoordTwoSize(i); j++) {

        currentCell = board.getCell(i, j);
        if (currentCell.isPositionInside(p) && !model.isGameOver()) {
          if (currentCell.isLegalMove() && !currentCell.isSelected()) {
            currentCell.select();
            System.out.println("Selected cell at (" + i + ", " + j + ")");
          } else {
            currentCell.deselect();
          }
        } else {
          board.getCell(i, j).deselect();
        }
      }
    }
    view.repaint();
  }
}
