package clock
import java.time.{Duration, Instant}

class TestClock(private var curTime : Instant) extends Clock {
  override def now(): Instant = curTime

  def setNow(newTime : Instant): Unit = {
    curTime = newTime
  }

  def addHours(hours : Int): Unit = {
    assert(0 < hours)
    curTime = curTime.plus(Duration.ofHours(hours))
  }

  def addMinutes(minutes : Int): Unit = {
    assert(0 < minutes)
    curTime = curTime.plus(Duration.ofMinutes(minutes))
  }
}
