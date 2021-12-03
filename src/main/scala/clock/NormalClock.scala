package clock
import java.time.Instant

class NormalClock extends Clock {
  override def now(): Instant = Instant.now()
}
