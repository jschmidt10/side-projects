defmodule IslandsEngine.BoardTest do
  use ExUnit.Case

  alias IslandsEngine.{Board, Coordinate, Island}

  test "should not allow positioning of overlapping islands" do
    square_island = create_island(:square, 1, 1)
    atoll_island = create_island(:atoll, 2, 2)

    board = Board.new()
    board = Board.position_island(board, :square, square_island)

    result = Board.position_island(board, :atoll, atoll_island)
    assert result == {:error, :overlapping_island}
  end

  test "should allow overlapping with same island" do
    square_island1 = create_island(:square, 1, 1)
    square_island2 = create_island(:square, 2, 1)

    board = Board.new()
    board = Board.position_island(board, :square, square_island1)
    board = Board.position_island(board, :square, square_island2)

    assert Map.has_key?(board, :square) == true
  end

  test "should realize when all islands are positioned" do
    square_island = create_island(:square, 1, 1)
    atoll_island = create_island(:atoll, 3, 3)
    dot_island = create_island(:dot, 10, 1)
    s_island = create_island(:s_shape, 6, 6)
    l_island = create_island(:l_shape, 8, 8)

    board = Board.new()

    board = Board.position_island(board, :square, square_island)
    assert Board.all_islands_positioned?(board) == false
    board = Board.position_island(board, :atoll, atoll_island)
    assert Board.all_islands_positioned?(board) == false
    board = Board.position_island(board, :dot, dot_island)
    assert Board.all_islands_positioned?(board) == false
    board = Board.position_island(board, :s_shape, s_island)
    assert Board.all_islands_positioned?(board) == false
    board = Board.position_island(board, :l_shape, l_island)
    assert Board.all_islands_positioned?(board) == true
  end

  test "should win if all placed islands are forested" do
    square_island = create_island(:square, 1, 1)

    board = Board.new()
    board = Board.position_island(board, :square, square_island)

    {:ok, c1} = Coordinate.new(1, 1)
    {:ok, c2} = Coordinate.new(1, 2)
    {:ok, c3} = Coordinate.new(2, 1)
    {:ok, c4} = Coordinate.new(2, 2)

    {:hit, :none, :no_win, board} = Board.guess(board, c1)
    {:hit, :none, :no_win, board} = Board.guess(board, c2)
    {:hit, :none, :no_win, board} = Board.guess(board, c3)
    {:hit, :square, :win, _board} = Board.guess(board, c4)
  end

  defp create_island(type, x, y) do
    with {:ok, upper_left} = Coordinate.new(x, y),
         {:ok, island} = Island.new(type, upper_left) do
      island
    end
  end
end
