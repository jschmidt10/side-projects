package com.github.jschmidt10.advent.day12;

public class Ship {

  private Direction facing;
  private int x;
  private int y;

  public Ship() {
    this.facing = Direction.EAST;
    this.x = 0;
    this.y = 0;
  }

  public void move(Direction direction, int num) {
    switch (direction) {
      case NORTH:
        y += num;
        break;
      case SOUTH:
        y -= num;
        break;
      case EAST:
        x += num;
        break;
      case WEST:
        x -= num;
        break;
      case FORWARD:
        move(facing, num);
        break;
      case LEFT:
        rotateLeft(num);
        break;
      case RIGHT:
        rotateRight(num);
        break;
      default:
        throw new IllegalArgumentException("Unknown direction: " + direction);
    }
  }

  private void rotateLeft(int num) {
    int rotations = (num % 360) / 90;
    for (int i = 0; i < rotations; i++) {
      facing = facing.rotateLeftOnce();
    }
  }

  private void rotateRight(int num) {
    int rotations = (num % 360) / 90;
    for (int i = 0; i < rotations; i++) {
      facing = facing.rotateRightOnce();
    }
  }

  public int getDistanceMoved() {
    return Math.abs(x) + Math.abs(y);
  }

}
