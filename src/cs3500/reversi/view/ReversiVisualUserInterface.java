package cs3500.reversi.view;

import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.interf.ReadonlyReversiModel;
import cs3500.reversi.model.position.CartesianPosition;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a visual GUI of the Reversi game.
 */
public class ReversiVisualUserInterface extends JPanel
    implements ReversiView, MouseListener, KeyListener {

  private final ReadonlyReversiModel model;
  private final ReversiController controller;
  private final int windowWidth;
  private final int windowHeight;


  /**
   * Constructs a new ReversiVisualUserInterface.
   *
   * @param model the game model
   * @param controller the game controller
   * @param windowWidth the width of the game window
   * @param windowHeight the height of the game window
   */
  public ReversiVisualUserInterface(ReadonlyReversiModel model, ReversiController controller,
      int windowWidth, int windowHeight) {
    this.model = model;
    this.controller = controller;

    if (windowWidth <= 0 || windowHeight <= 0) {
      throw new IllegalArgumentException("windowWidth and windowHeight must be greater than 0");
    }

    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;

    setPreferredSize(new Dimension(windowWidth, windowHeight));
    setBackground(Color.DARK_GRAY);
    addMouseListener(this);
    addKeyListener(this);

  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    model.paint(g, Math.floorDiv(windowWidth, 2), Math.floorDiv(windowHeight, 2));
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    controller.handleMouseInput(new CartesianPosition(getCoordinateX(e), getCoordinateY(e)));
  }

  private double getCoordinateX(MouseEvent e) {
    return e.getX() - windowWidth / 2;
  }

  private double getCoordinateY(MouseEvent e) {
    return windowHeight / 2 - e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // Do nothing
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Do nothing
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // Do nothing
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // Do nothing
  }

  @Override
  public void keyPressed(KeyEvent e) {
    controller.handleKeyInput(e.getKeyCode());
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Do nothing
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Do nothing
  }
}

