package statistics

import clock.Clock

import java.time.{Duration, Instant}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class StatCounter(val clock: Clock) extends EventStatistic {
  private val statistics = mutable.Map[String, ArrayBuffer[Instant]]().withDefaultValue(ArrayBuffer.empty)
  private val MINUTES_IN_HOUR = 60.0

  override def incEvent(name: String): Unit = {
    statistics += Tuple2(name, statistics(name) :+ clock.now())
  }

  override def getEventStatisticByName(name: String): Double = {
    val hourAgo = clock.now().minus(Duration.ofHours(1))
    getStatStartingFrom(name, hourAgo)
  }

  override def getAllEventStatistic: Predef.Map[String, Double] = {
    getAllIterableStat.toMap
  }

  override def printStatistic(): Unit = getAllIterableStat.foreach {
    pair => println(s"${pair._1} -> ${pair._2} rpm")
  }

  private def getStatStartingFrom(name: String, startFrom: Instant): Double = {
    statistics += Tuple2(name, statistics(name).filter(time => time.isAfter(startFrom)))
    statistics(name).size.toDouble / MINUTES_IN_HOUR
  }

  private def getAllIterableStat: Iterable[(String, Double)] = {
    val hourAgo = clock.now().minus(Duration.ofHours(1))
    statistics
      .keys
      .map(name => (name, getStatStartingFrom(name, hourAgo)))
  }
}
