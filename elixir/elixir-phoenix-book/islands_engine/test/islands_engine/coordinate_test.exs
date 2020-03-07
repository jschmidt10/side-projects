defmodule IslandsEngine.CoordinateTest do
  use ExUnit.Case

  alias IslandsEngine.Coordinate

  test "creates valid coordinates" do
    result = Coordinate.new(1, 1)
    assert elem(result, 0) == :ok
  end

  test "errors on invalid coordiante" do
    assert Coordinate.new(0, 0) == {:error, :invalid_coordinate}
    assert Coordinate.new(1, 11) == {:error, :invalid_coordinate}
  end
end
