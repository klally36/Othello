package cs3500.reversi.model.position;

/**
 * Represents a position in a 2D space using Cartesian coordinates.
 * The position is defined by x and y coordinates.
 */
public class CartesianPosition extends AbstractPosition {

  private final double x;

  private final double y;

  /**
   * Constructs a new CartesianPosition with the given x and y coordinates.
   *
   * @param x the x-coordinate of the position
   * @param y the y-coordinate of the position
   */
  public CartesianPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructs a new CartesianPosition by copying the values from another position.
   *
   * @param positionToCopy the position to copy
   */
  public CartesianPosition(CartesianPosition positionToCopy) {
    this.x = positionToCopy.x;
    this.y = positionToCopy.y;
  }

  @Override
  public boolean equals(Object other) {
    CartesianPosition that;
    if (other instanceof PolarPosition) {
      PolarPosition otherPolarPosition = (PolarPosition) other;
      that = otherPolarPosition.toCartesianPosition();
      return Math.abs(this.x - that.x) < 0.0001 && Math.abs(this.y - that.y) < 0.0001;
    } else if (other instanceof CartesianPosition) {
      that = (CartesianPosition) other;
      return Math.abs(this.x - that.x) < 0.0001 && Math.abs(this.y - that.y) < 0.0001;
    }
    else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "CartesianPosition{" + "x=" + x + ", y=" + y + '}';
  }

  @Override
  public int hashCode() {
    return (int) (x * 1000 + y * 1000);
  }

  @Override
  public Position subtract(Position otherPosition) throws IllegalArgumentException {
    if (otherPosition == null) {
      throw new IllegalArgumentException("otherPosition cannot be null");
    }
    CartesianPosition that;
    if (otherPosition instanceof CartesianPosition) {
      that = (CartesianPosition) otherPosition;
    } else {
      that = ((PolarPosition) otherPosition).toCartesianPosition();
    }
    return new CartesianPosition(this.x - that.x, this.y - that.y);
  }

  @Override
  public Position add(Position otherPosition) throws IllegalArgumentException {
    if (otherPosition == null) {
      throw new IllegalArgumentException("otherPosition cannot be null");
    }
    CartesianPosition that;
    if (otherPosition instanceof CartesianPosition) {
      that = (CartesianPosition) otherPosition;
    } else {
      that = ((PolarPosition) otherPosition).toCartesianPosition();
    }
    return new CartesianPosition(this.x + that.x, this.y + that.y);
  }

  @Override
  public boolean rightLineCrossesSegment(Position startPosition, Position endPosition)
      throws IllegalArgumentException {
    if (startPosition == null || endPosition == null) {
      throw new IllegalArgumentException("startPosition and endPosition cannot be null");
    }
    CartesianPosition startCartesianPosition;
    CartesianPosition endCartesianPosition;

    if (startPosition instanceof CartesianPosition) {
      startCartesianPosition = (CartesianPosition) startPosition;
    } else {
      startCartesianPosition = ((PolarPosition) startPosition).toCartesianPosition();
    }

    if (endPosition instanceof CartesianPosition) {
      endCartesianPosition = (CartesianPosition) endPosition;
    } else {
      endCartesianPosition = ((PolarPosition) endPosition).toCartesianPosition();
    }

    return (this.x < Math.max(startCartesianPosition.x, endCartesianPosition.x))
        && (this.y > Math.min(startCartesianPosition.y, endCartesianPosition.y))
        && (this.y <= Math.max(startCartesianPosition.y, endCartesianPosition.y));
  }

  @Override
  public CartesianPosition toCartesianPosition() {
    return new CartesianPosition(this);
  }

  @Override
  public PolarPosition toPolarPosition() {
    return new PolarPosition(Math.sqrt(x * x + y * y), Math.atan2(y, x));
  }

  @Override
  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  @Override
  public double getPixelX(int xCenter) {
    if (xCenter < 0) {
      throw new IllegalArgumentException("xCenter cannot be less than 0");
    }
    return xCenter + x;
  }

  @Override
  public double getPixelY(int yCenter) {
    if (yCenter < 0) {
      throw new IllegalArgumentException("yCenter cannot be less than 0");
    }
    return yCenter - y;
  }

}