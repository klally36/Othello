package cs3500.reversi.model.position;


/**
 * Represents a position in a 2D space.
 */
public interface Position {

  /**
   * Returns the distance between this position and the other position.
   * 
   * @param otherPosition the other position to calculate the distance to
   * @return distance between this position and the other position
   * @throws IllegalArgumentException if otherPosition is null
   */
  double distanceTo(Position otherPosition) throws IllegalArgumentException;

  /**
   * Returns the magnitude of this position as a vector.
   * 
   * @return magnitude of this position as a vector
   */
  double magnitude();

  /**
   * Calculate the difference between this position and the other position.
   * 
   * @return difference between this position and the other position as a Position
   * @throws IllegalArgumentException if otherPosition is null
   */
  Position subtract(Position otherPosition) throws IllegalArgumentException;


  /**
   * Calculate the sum of this position and the other position.
   * 
   * @return sum of this position and the other position as a Position
   * @throws IllegalArgumentException if otherPosition is null
   */
  Position add(Position otherPosition) throws IllegalArgumentException;

  /**
   * Returns if the ray from this position starts from the right crosses the line segment from
   * startPosition to endPosition.
   * 
   * @return if the ray from this position starts from the right crosses the line segment from
   *         startPosition to endPosition
   * @throws IllegalArgumentException if startPosition or endPosition is null
   */
  boolean rightLineCrossesSegment(Position startPosition, Position endPosition)
      throws IllegalArgumentException;


  /**
   * Get the x pixel coordinate of this position.
   * 
   * @param xCenter the x pixel coordinate of the center of the board
   * @return the x pixel coordinate of this position
   * @throws IllegalArgumentException if xCenter is less than 0
   */
  double getPixelX(int xCenter) throws IllegalArgumentException;


  /**
   * Get the y pixel coordinate of this position.
   * 
   * @param yCenter the y pixel coordinate of the center of the board
   * @return the y pixel coordinate of this position
   * @throws IllegalArgumentException if yCenter is less than 0
   */
  double getPixelY(int yCenter);

}
