defmodule IslandsEngine.RulesTest do
  use ExUnit.Case

  alias IslandsEngine.Rules

  test "cannot position islands after they are set" do
    rules = Rules.new()
    rules = %{rules | state: :players_set}

    {:ok, rules} = Rules.check(rules, {:position_islands, :player1})
    {:ok, rules} = Rules.check(rules, {:set_islands, :player1})

    assert Rules.check(rules, {:position_islands, :player1}) == :error
  end

  test "after both players set islands it should be player1 turn" do
    rules = Rules.new()
    rules = %{rules | state: :players_set}

    {:ok, rules} = Rules.check(rules, {:set_islands, :player1})
    {:ok, rules} = Rules.check(rules, {:set_islands, :player2})

    assert rules.state == :player1_turn
  end

  test "should alternate between player1 and player2 turns" do
    rules = Rules.new()
    rules = %{rules | state: :player1_turn}

    {:ok, rules} = Rules.check(rules, {:guess_coordinate, :player1})
    assert rules.state == :player2_turn

    {:ok, rules} = Rules.check(rules, {:guess_coordinate, :player2})
    assert rules.state == :player1_turn
  end

  test "should transition to game over after a win" do
    rules = Rules.new()
    rules = %{rules | state: :player1_turn}

    {:ok, rules} = Rules.check(rules, {:win_check, :win})

    assert rules.state == :game_over
  end
end
