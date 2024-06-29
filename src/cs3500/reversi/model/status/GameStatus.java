package cs3500.reversi.model.status;

/**
 * Represents the status of the game. The states the game can be in. WTurn means that player 2 is
 * placing a white piece. BTurn means that player 1 is placing a black piece. (if using AI this will
 * be instant) Draw means the game has ended due to all positions being filled. WWins means the
 * white had more pieces at the end of the game. BWins means the black had more pieces at the end of
 * the game.
 */

public enum GameStatus {
  WTURN, BTURN, DRAW, WWINS, BWINS
}
