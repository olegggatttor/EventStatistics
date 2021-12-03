package statistics

trait EventStatistic {
  def incEvent(name : String)

  def getEventStatisticByName(name : String) : Double

  def getAllEventStatistic: Map[String, Double]

  def printStatistic()
}
