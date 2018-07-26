import examples.CachedResourceIntensiveOperation
import examples.SimpleCachedResourceIntensiveOperation
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

internal class LazyCachedTest {

    /**
     * In this case CachedResourceIntensiveOperation represents some resource-intensive
     * [CachedResourceIntensiveOperation.sleepTime] is just time in milliseconds of thread sleeping
     */
    @Test
    public fun cachedResourceIntensiveOperationShouldCache() {
        val firstSleepMs = 1500L
        val secondSleepMs = 1000L


        val cachedPowerOperation = CachedResourceIntensiveOperation(firstSleepMs)

        val checkCalcAndPrint = { measureTimeMillis { cachedPowerOperation.result }.also { println(it) } }

        val shouldNotBeCached = checkCalcAndPrint()
        assert(shouldNotBeCached >= firstSleepMs)

        val shouldBeCached = checkCalcAndPrint()
        assert(shouldBeCached < firstSleepMs)

        cachedPowerOperation.sleepTime = secondSleepMs

        val shouldNotBeCachedAfterChange = checkCalcAndPrint()
        assert(shouldNotBeCachedAfterChange >= secondSleepMs)

        val shouldBeCachedAgain = checkCalcAndPrint()
        assert(shouldBeCachedAgain < secondSleepMs)


    }

    /**
     * In this case CachedResourceIntensiveOperation represents some resource-intensive
     * [SimpleCachedResourceIntensiveOperation.sleepTime] is just time in milliseconds of thread sleeping
     */
    @Test
    public fun simpleCachedResourceIntensiveOperationShouldCache() {
        val firstSleepMs = 1500L
        val secondSleepMs = 1000L


        val cachedPowerOperation = SimpleCachedResourceIntensiveOperation(firstSleepMs)

        val checkCalcAndPrint = { measureTimeMillis { cachedPowerOperation.result }.also { println(it) } }

        val shouldNotBeCached = checkCalcAndPrint()
        assert(shouldNotBeCached >= firstSleepMs)

        val shouldBeCached = checkCalcAndPrint()
        assert(shouldBeCached < firstSleepMs)

        cachedPowerOperation.sleepTime = secondSleepMs

        val shouldNotBeCachedAfterChange = checkCalcAndPrint()
        assert(shouldNotBeCachedAfterChange >= secondSleepMs)

        val shouldBeCachedAgain = checkCalcAndPrint()
        assert(shouldBeCachedAgain < secondSleepMs)


    }

}