package com.github.jschmidt10.advent.day12;

public class WaypointShip {

  private Point waypoint;
  private Point ship;

  public WaypointShip(Point waypoint) {
    this.waypoint = waypoint;
    this.ship = new Point(0, 0);
  }

  public void move(Direction direction, int num) {
    switch (direction) {
      case NORTH:
        waypoint = waypoint.translate(0, num);
        break;
      case SOUTH:
        waypoint = waypoint.translate(0, -num);
        break;
      case EAST:
        waypoint = waypoint.translate(num, 0);
        break;
      case WEST:
        waypoint = waypoint.translate(-num, 0);
        break;
      case FORWARD:
        ship = moveShip(num);
        break;
      case LEFT:
        waypoint = rotateWaypointLeft(num);
        break;
      case RIGHT:
        waypoint = rotateWaypointRight(num);
        break;
      default:
        throw new IllegalArgumentException("Unknown direction: " + direction);
    }
  }

  private Point moveShip(int multiplier) {
    Vector move = new Vector(waypoint.x, waypoint.y);
    move = move.scale(multiplier);
    return ship.translate(move);
  }

  private Point rotateWaypointLeft(int degrees) {
    Point nextWaypoint = waypoint;
    int rotations = (degrees % 360) / 90;
    for (int i = 0; i < rotations; i++) {
      nextWaypoint = nextWaypoint.rotateLeft();
    }
    return nextWaypoint;
  }

  private Point rotateWaypointRight(int degrees) {
    Point nextWaypoint = waypoint;
    int rotations = (degrees % 360) / 90;
    for (int i = 0; i < rotations; i++) {
      nextWaypoint = nextWaypoint.rotateRight();
    }
    return nextWaypoint;
  }

  public int getDistanceMoved() {
    return Math.abs(ship.x) + Math.abs(ship.y);
  }

  @Override
  public String toString() {
    return "WaypointShip{" +
        "waypoint=" + waypoint +
        ", ship=" + ship +
        '}';
  }
}
