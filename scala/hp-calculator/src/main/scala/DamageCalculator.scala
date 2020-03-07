
/**
  * Utility for computing attacks necessary to deal a particular amount of damage.
  */
object DamageCalculator {

  def calculate(target: Int, options: List[Int]): Unit = {
    calculate(target, options.sorted, List())
  }

  private def calculate(target: Int, options: List[Int], used: List[Int]): Unit = {
    if (target == 0) {
      println(used)
    }
    else if (options.nonEmpty && target > 0) {
      var rest = options
      while (rest.nonEmpty) {
        val head = rest.head
        calculate(target - head, rest, head :: used)
        rest = rest.tail
      }
    }
  }
  def main(args: Array[String]): Unit = {
    DamageCalculator.calculate(254, List(262, 369, 242, 83, 123, 107, 392, 44, 77, 76, 87, 287))
  }
}
