defmodule IslandsEngine.IslandTest do
  use ExUnit.Case

  alias IslandsEngine.{Coordinate, Island}

  test "should create valid island" do
    {:ok, upper_left} = Coordinate.new(1, 1)
    result = Island.new(:square, upper_left)

    assert elem(result, 0) == :ok
  end

  test "should error when island is out of bounds" do
    {:ok, upper_left} = Coordinate.new(10, 10)
    assert Island.new(:square, upper_left) == {:error, :invalid_coordinate}
  end

  test "should be forested? when all coordinates hit" do
    {:ok, upper_left} = Coordinate.new(1, 1)
    {:ok, dot_island} = Island.new(:dot, upper_left)

    assert Island.forested?(dot_island) == false

    {:hit, dot_island} = Island.guess(dot_island, upper_left)

    assert Island.forested?(dot_island) == true
  end

  test "should overlaps? when coordinates are shared" do
    {:ok, upper_left} = Coordinate.new(1, 1)
    {:ok, square_island} = Island.new(:square, upper_left)
    {:ok, dot_island} = Island.new(:dot, upper_left)

    assert Island.overlaps?(square_island, dot_island) == true

    {:ok, upper_left} = Coordinate.new(8, 8)
    {:ok, s_island} = Island.new(:s_shape, upper_left)

    assert Island.overlaps?(square_island, s_island) == false
  end
end
