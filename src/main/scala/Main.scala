import clock.NormalClock
import statistics.{EventStatistic, StatCounter}

object Main {
  def main(args: Array[String]): Unit = {
    val clock = new NormalClock()
    val stat : EventStatistic = new StatCounter(clock)


    stat.printStatistic()
    stat.incEvent("click")
    stat.incEvent("close browser")
    stat.incEvent("click")
    println(stat.getAllEventStatistic)
    stat.printStatistic()
  }
}
