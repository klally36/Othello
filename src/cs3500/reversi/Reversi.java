package cs3500.reversi;

import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.controller.ReversiVisualController;
import cs3500.reversi.model.implem.AdvancedAIPlayer;
import cs3500.reversi.model.implem.HexagonReversi;
import cs3500.reversi.model.implem.HumanPlayer;
import cs3500.reversi.model.implem.SquareReversi;
import cs3500.reversi.model.interf.Player;
import cs3500.reversi.model.interf.ReversiModel;

/**
 * Represents the main class of the Reversi game.
 */
public final class Reversi {

  private static final int PANEL_HEIGHT = 1000;
  private static final int PANEL_WIDTH = 900;

  /**
   * The main method of the Reversi game.
   *
   * @param args Not set.
   */
  public static void main(String[] args) {

    String board;
    int size;
    int radius;
    String player;
    int corner;
    int phobia;
    int greedy;
    int minimax;

    if (args.length == 4) {
      board = args[0];
      size = Integer.parseInt(args[1]);
      radius = Integer.parseInt(args[2]);
      player = args[3];
      
      
      if (!board.equals("hexa") && !board.equals("square")) {
        throw new IllegalArgumentException(
            "The board of the game has to be either 'hexa' or 'square', get " + board);
      }
      if (!player.equals("human") && !player.equals("ai")) {
        throw new IllegalArgumentException(
            "The oponent of the game has to be either 'human' or 'ai', get " + player);
      }

      corner = 1;
      phobia = 1;
      greedy = 1;
      minimax = 1;

    } else if (args.length == 8) {

      board = args[0];
      size = Integer.parseInt(args[1]);
      radius = Integer.parseInt(args[2]);
      player = args[3];

      corner = Integer.parseInt(args[4]);
      phobia = Integer.parseInt(args[5]);
      greedy = Integer.parseInt(args[6]);
      minimax = Integer.parseInt(args[7]);

      if (!board.equals("hexa") && !board.equals("square")) {
        throw new IllegalArgumentException(
            "The board of the game has to be either 'hexa' or 'square', get " + board);
      }
      if (!player.equals("ai")) {
        throw new IllegalArgumentException(
            "The oponent of the game has to be 'ai' to set strategies preference, get " + player);
      }

    } else {
      throw new IllegalArgumentException("Invalid number of argument!");
    }

    Player oponent;
    ReversiModel model;

    if (player.equals("human")) {
      oponent = new HumanPlayer();
    } else {
      if (board.equals("hexa")) {
        oponent = new AdvancedAIPlayer(6, corner, phobia, greedy, minimax);
      } else {
        oponent = new AdvancedAIPlayer(4, corner, phobia, greedy, minimax);
      }
    }

    if (board.equals("hexa")) {
      model = new HexagonReversi(size, radius, Math.PI / 2, new HumanPlayer(), oponent);
    } else {
      model = new SquareReversi(size, radius, Math.PI / 4, new HumanPlayer(), oponent);
    }
    ReversiController controller = new ReversiVisualController();
    controller.playGame(model, PANEL_HEIGHT, PANEL_WIDTH);
  }
}
