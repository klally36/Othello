package cs3500.reversi.model.implem;

import java.awt.Graphics;
import cs3500.reversi.model.position.PolarPosition;
import cs3500.reversi.model.position.Position;
import cs3500.reversi.model.status.CellStatus;
import cs3500.reversi.model.interf.Cell;

import java.awt.Color;

/**
 * Represents a cell in a polygonal game board. The cell is polygonal in shape and can hold a game
 * piece. The cell can be empty, hold a black piece, or hold a white piece.
 */
public class PolygonCell implements Cell {

  private final Position center;

  private final int sides;

  private final Position[] vertices;

  private boolean isLegalMove;

  private boolean selected;

  private CellStatus cellStatus;

  private final double orientation;

  private final double radius;

  /**
   * Constructs a polygonal cell with the given parameters.
   * 
   * @param center the center position of the cell
   * @param radius the radius of the cell (distance from the center to any vertex of the cell)
   * @param sides the number of sides of the cell
   * @param orientation the orientation of the cell in radians, counter-clockwise from the positive
   *        x-axis
   * @throws IllegalArgumentException if radius is less than or equal to 0
   */
  public PolygonCell(Position center, double radius, int sides, double orientation) {
    this.center = center;
    this.sides = sides;
    this.cellStatus = CellStatus.EMPTY;
    this.isLegalMove = false;
    this.selected = false;
    this.orientation = orientation;

    if (radius <= 0) {
      throw new IllegalArgumentException("radius must be greater than 0");
    }

    if (sides < 3) {
      throw new IllegalArgumentException("sides must be greater than or equal to 3");
    }

    this.radius = radius;
    vertices = new Position[sides];
    for (int i = 0; i < sides; i++) {
      vertices[i] =
          new PolarPosition(radius, (orientation + i * 2 * Math.PI / sides) % (2 * Math.PI))
              .add(center);
    }
  }

  /**
   * Constructs a polygonal cell with the same parameters as the given cell.
   * 
   * @param that the cell to copy
   */
  public PolygonCell(Cell that) {
    if (that == null) {
      throw new IllegalArgumentException("that cannot be null");
    } else if (!(that instanceof PolygonCell)) {
      throw new IllegalArgumentException("that must be a PolygonCell");
    }

    PolygonCell thatPolygonCell = (PolygonCell) that;
    this.center = thatPolygonCell.center;
    this.sides = thatPolygonCell.sides;
    this.cellStatus = thatPolygonCell.cellStatus;
    this.isLegalMove = thatPolygonCell.isLegalMove;
    this.selected = thatPolygonCell.selected;
    this.orientation = thatPolygonCell.orientation;
    this.radius = thatPolygonCell.radius;
    this.vertices = new Position[sides];
    for (int i = 0; i < sides; i++) {
      vertices[i] =
          new PolarPosition(radius, (orientation + i * 2 * Math.PI / sides) % (2 * Math.PI))
              .add(center);
    }
  }

  @Override
  public boolean isPositionInside(Position targetPosition) {
    if (targetPosition == null) {
      throw new IllegalArgumentException("targetPosition cannot be null");
    }
    int intersections = 0;

    Position start;
    Position end;

    for (int i = 0; i < sides; i++) {
      start = vertices[i];
      end = vertices[(i + 1) % sides];
      if (targetPosition.rightLineCrossesSegment(start, end)) {
        intersections++;
      }
    }
    return intersections % 2 == 1;
  }

  @Override
  public CellStatus getCellStatus() {
    return cellStatus;
  }

  @Override
  public void select() {
    selected = true;
  }

  @Override
  public void deselect() {
    selected = false;
  }

  @Override
  public boolean isSelected() {
    return selected;
  }

  @Override
  public void setCellStatus(CellStatus cellStatus) throws IllegalArgumentException {
    if (cellStatus == CellStatus.EMPTY) {
      throw new IllegalArgumentException("cellStatus cannot be set to EMPTY");
    }
    this.cellStatus = cellStatus;
  }

  @Override
  public Position getPosition() {
    return center;
  }

  @Override
  public Position getAdjacentCellCenter(int index) throws IllegalArgumentException {
    if (index < 0 || index >= sides) {
      throw new IllegalArgumentException("index must be between 0 and " + (sides - 1) + ", get"
              + index) ;
    }
    double centerToCenterDist = 2 * radius * Math.cos(Math.PI / sides);
    return new PolarPosition(centerToCenterDist,
        (orientation + Math.PI / sides + index * 2 * Math.PI / sides)
                % (2 * Math.PI)).add(center);
  }

  @Override
  public void flip() throws IllegalArgumentException {
    if (cellStatus == CellStatus.EMPTY) {
      throw new IllegalArgumentException("cellStatus cannot be set to EMPTY");
    }
    if (cellStatus == CellStatus.BLACK) {
      cellStatus = CellStatus.WHITE;
    } else {
      cellStatus = CellStatus.BLACK;
    }
  }


  @Override
  public boolean isLegalMove() {
    return isLegalMove;
  }

  @Override
  public void setLegalMove(boolean legalMove) {
    isLegalMove = legalMove;
  }

  @Override
  public void paint(Graphics g, int xCenter, int yCenter) throws IllegalArgumentException {
    if (g == null) {
      throw new IllegalArgumentException("g cannot be null");
    }
    if (this.isLegalMove()) {
      if (this.selected) {
        g.setColor(Color.CYAN);
      } else {
        g.setColor(Color.GREEN);
      }
    } else {
      g.setColor(Color.GRAY);
    }
    int[] xPoints = new int[sides];
    int[] yPoints = new int[sides];
    for (int i = 0; i < sides; i++) {
      xPoints[i] = (int) vertices[i].getPixelX(xCenter);
      yPoints[i] = (int) vertices[i].getPixelY(yCenter);
    }
    g.fillPolygon(xPoints, yPoints, sides);
    g.setColor(Color.BLACK);
    g.drawPolygon(xPoints, yPoints, sides);

    if (cellStatus == CellStatus.BLACK) {
      g.setColor(Color.BLACK);
      g.fillOval((int) center.getPixelX(xCenter) - (int) radius / 2,
          (int) center.getPixelY(yCenter) - (int) radius / 2, (int) radius, (int) radius);
    } else if (cellStatus == CellStatus.WHITE) {
      g.setColor(Color.WHITE);
      g.fillOval((int) center.getPixelX(xCenter) - (int) radius / 2,
          (int) center.getPixelY(yCenter) - (int) radius / 2, (int) radius, (int) radius);
    }
  }
}
