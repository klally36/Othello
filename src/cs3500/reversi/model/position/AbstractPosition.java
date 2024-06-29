package cs3500.reversi.model.position;

/**
 * Abstract class for a position in a 2D space.
 * This class provides a common base for different representations of 2D positions.
 */
public abstract class AbstractPosition implements Position {

  @Override
  public double distanceTo(Position otherPosition) throws IllegalArgumentException {
    if (otherPosition == null) {
      throw new IllegalArgumentException("otherPosition cannot be null");
    }
    CartesianPosition that;
    if (otherPosition instanceof PolarPosition) {
      PolarPosition otherPolarPosition = (PolarPosition) otherPosition;
      that = otherPolarPosition.toCartesianPosition();

    } else  {
      that = (CartesianPosition) otherPosition;
    }
    return this.subtract(that).magnitude();
  }

  /**
   * Converts this position to a Cartesian position.
   *
   * @return the Cartesian representation of this position
   */
  public abstract CartesianPosition toCartesianPosition();

  /**
   * Converts this position to a Polar position.
   *
   * @return the Polar representation of this position
   */
  public abstract PolarPosition toPolarPosition();

  
}