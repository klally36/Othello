package cs3500.reversi.model.position;

/**
 * Represents a position in a 2D space using polar coordinates.
 * The position is defined by a radius and an angle.
 */
public class PolarPosition extends AbstractPosition {

  private final double radius;

  private final double angle;

  /**
   * Constructs a new PolarPosition with the given radius and angle.
   *
   * @param radius the radius of the position
   * @param angle the angle of the position in radians, counter-clockwise from the positive x-axis
   * @throws IllegalArgumentException if radius is less than 0
   */
  public PolarPosition(double radius, double angle) {

    if (radius < 0) {
      throw new IllegalArgumentException("radius must be greater than 0");
    }
    this.radius = radius;
    this.angle = angle % (2 * Math.PI);
  }

  /**
   * Constructs a new PolarPosition by copying the values from another position.
   *
   * @param positionToCopy the position to copy
   */
  public PolarPosition(PolarPosition positionToCopy) {
    this.radius = positionToCopy.radius;
    this.angle = positionToCopy.angle;
  }

  @Override
  public Position subtract(Position otherPosition) throws IllegalArgumentException {
    if (otherPosition == null) {
      throw new IllegalArgumentException("otherPosition cannot be null");
    }
    return this.toCartesianPosition().subtract(otherPosition);
  }

  @Override
  public Position add(Position otherPosition) throws IllegalArgumentException {
    if (otherPosition == null) {
      throw new IllegalArgumentException("otherPosition cannot be null");
    }
    return this.toCartesianPosition().add(otherPosition);
  }

  @Override
  public boolean rightLineCrossesSegment(Position startPosition, Position endPosition)
      throws IllegalArgumentException {
    if (startPosition == null || endPosition == null) {
      throw new IllegalArgumentException("startPosition and endPosition cannot be null");
    }
    CartesianPosition thisCartesianPosition = this.toCartesianPosition();
    return thisCartesianPosition.rightLineCrossesSegment(startPosition, endPosition);
  }

  @Override
  public boolean equals(Object other) {
    PolarPosition that;
    if (other instanceof CartesianPosition) {
      CartesianPosition otherCartesianPosition = (CartesianPosition) other;
      that = otherCartesianPosition.toPolarPosition();
      return Math.abs(this.radius - that.radius) < 0.0001
          && Math.abs(this.angle - that.angle) < 0.0001;
    } else if (other instanceof PolarPosition) {
      that = (PolarPosition) other;
      return Math.abs(this.radius - that.radius) < 0.0001
          && Math.abs(this.angle - that.angle) < 0.0001;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return (int) (radius * 1000 + angle * 1000);
  }

  @Override
  public String toString() {
    return "PolarPosition{" + "radius=" + radius + ", angle=" + angle / (2 * Math.PI) + '}';
  }


  @Override
  public CartesianPosition toCartesianPosition() {
    return new CartesianPosition(radius * Math.cos(angle), radius * Math.sin(angle));
  }


  @Override
  public PolarPosition toPolarPosition() {
    return new PolarPosition(this);
  }

  /**
   * Returns the magnitude of this position, which is the radius.
   *
   * @return the radius of this position
   */
  @Override
  public double magnitude() {
    return radius;
  }

  @Override
  public double getPixelX(int xCenter) {
    return this.toCartesianPosition().getPixelX(xCenter);
  }

  @Override
  public double getPixelY(int yCenter) {
    return this.toCartesianPosition().getPixelY(yCenter);
  }
}