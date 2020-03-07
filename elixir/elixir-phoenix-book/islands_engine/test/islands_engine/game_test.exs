defmodule IslandsEngine.GameTest do
  use ExUnit.Case

  alias IslandsEngine.{Game, Rules}

  test "should add second player" do
    {:ok, game} = Game.start_link("player1")

    assert :sys.get_state(game).player1.name == "player1"
    assert :sys.get_state(game).player2.name == nil

    assert Game.add_player(game, "player2") == :ok

    assert :sys.get_state(game).player2.name == "player2"
  end

  test "should position islands" do
    game = create_game()

    assert Game.position_island(game, :player1, :square, 1, 1) == :ok
    assert Game.position_island(game, :player2, :dot, 1, 1) == :ok

    state = :sys.get_state(game)
    assert Map.has_key?(state.player1.board, :square)
    assert Map.has_key?(state.player2.board, :dot)
  end

  test "should prevent invalid islands" do
    game = create_game()
    assert Game.position_island(game, :player1, :wrong, 1, 1) == {:error, :invalid_island_type}
  end

  test "should prevent invalid coordinates" do
    game = create_game()
    assert Game.position_island(game, :player1, :square, 0, 1) == {:error, :invalid_coordinate}
  end

  test "should require all islands positioned before setting islands" do
    game = create_game()

    assert Game.set_islands(game, :player1) == {:error, :not_all_islands_positioned}

    :ok = Game.position_island(game, :player1, :square, 1, 1)
    :ok = Game.position_island(game, :player1, :dot, 3, 3)
    :ok = Game.position_island(game, :player1, :l_shape, 4, 4)
    :ok = Game.position_island(game, :player1, :s_shape, 1, 7)
    :ok = Game.position_island(game, :player1, :atoll, 7, 7)

    result = Game.set_islands(game, :player1)
    assert elem(result, 0) == :ok
  end

  test "should returned missed guesses" do
    game = create_single_island_game()
    assert Game.guess_coordinate(game, :player1, 2, 2) == {:miss, :none, :no_win}
  end

  test "should detect guesses that hit and win" do
    game = create_single_island_game()
    assert Game.guess_coordinate(game, :player1, 1, 1) == {:hit, :dot, :win}
  end

  defp create_single_island_game() do
    game = create_game()

    Game.position_island(game, :player1, :dot, 1, 1)
    Game.position_island(game, :player2, :dot, 1, 1)

    :sys.replace_state(game, fn state -> %{state | rules: %Rules{state: :player1_turn}} end)

    game
  end

  defp create_game() do
    {:ok, game} = Game.start_link("player1")
    :ok = Game.add_player(game, "player2")
    game
  end
end
