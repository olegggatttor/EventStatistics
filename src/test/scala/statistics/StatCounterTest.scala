package statistics

import clock.TestClock
import org.scalatestplus.play.PlaySpec

import java.time.Instant

class StatCounterTest extends PlaySpec {
  private val TEST_EVENT_NAME_1 = "test_1"
  private val TEST_EVENT_NAME_2 = "test_2"

  "statistics.StatCounter" must {
    "return empty map when there is no events" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.getAllEventStatistic mustEqual Map.empty
    }

    "return correct number for single event for single inc" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 1.0 / 60.0
    }

    "return correct number for single event if event was more than hour ago" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addHours(1)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 0.0
    }

    "return correct number for single event for multiple increments" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(30)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(15)

      counter.incEvent(TEST_EVENT_NAME_1)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 3.0 / 60.0
    }

    "return correct number for single event for multiple increments " +
      "even if of the event took place more than hour ago" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(30)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(15)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(20)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 2.0 / 60.0
    }

    "return correct number for multiple events for single inc" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)
      counter.incEvent(TEST_EVENT_NAME_2)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 1.0 / 60.0
      counter.getEventStatisticByName(TEST_EVENT_NAME_2) mustEqual 1.0 / 60.0
      counter.getAllEventStatistic mustEqual Seq(TEST_EVENT_NAME_1 -> 1.0 / 60.0, TEST_EVENT_NAME_2 -> 1.0 / 60.0).toMap
    }

    "return correct number for multiple events for multiple incs and delays" in {
      val customClock = new TestClock(Instant.EPOCH)
      val counter = new StatCounter(customClock)

      counter.incEvent(TEST_EVENT_NAME_1)

      customClock.addMinutes(20)

      counter.incEvent(TEST_EVENT_NAME_2)

      customClock.addMinutes(40)

      counter.incEvent(TEST_EVENT_NAME_1)
      counter.incEvent(TEST_EVENT_NAME_2)

      counter.getEventStatisticByName(TEST_EVENT_NAME_1) mustEqual 1.0 / 60.0
      counter.getEventStatisticByName(TEST_EVENT_NAME_2) mustEqual 2.0 / 60.0
      counter.getAllEventStatistic mustEqual Seq(TEST_EVENT_NAME_1 -> 1.0 / 60.0, TEST_EVENT_NAME_2 -> 2.0 / 60.0).toMap
    }
  }
}