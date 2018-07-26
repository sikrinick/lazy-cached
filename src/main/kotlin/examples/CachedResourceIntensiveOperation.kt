package examples

/**
 * Used for testing purposes.
 * Represents some operation, that requires a lot of time to execute with caching and lazy invocation.
 * On calling [result] it calls [calculate] or, if cached, just returns result.
 * On changing [sleepTime] next [result] call should recalculate again.
 * In this case, [calculate] just forces thread to sleep for [sleepTime] milliseconds.
 * @param sleepTime time in milliseconds for thread sleep.
 */
class CachedResourceIntensiveOperation(sleepTime: Long) : Calculable<Long>() {

    /**
     * Time in milliseconds for thread to sleep
     * Not final!
     */
    public var sleepTime by Calculable.Cacheable(sleepTime)

    /**
     * Forces thread to sleep, basically, if [cached] is False
     */
    override fun calculate(): Long {
        Thread.sleep(sleepTime)
        return sleepTime
    }

}