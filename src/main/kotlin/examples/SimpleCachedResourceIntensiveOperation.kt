package examples

import lazyCached

/**
 * Used for testing purposes.
 * Represents some operation, that requires a lot of time to execute with caching and lazy invocation.
 * On calling [result] it just forces thread to sleep for [sleepTime] milliseconds.
 * On changing [sleepTime] next [result] call should recalculate again.
 * @param startingSleepTime time in milliseconds for thread sleep.
 */
class SimpleCachedResourceIntensiveOperation(startingSleepTime: Long) {

    /**
     * Flag for caching
     */
    private var cached = false

    /**
     * Time in milliseconds for thread to sleep
     * Not final!
     */
    public var sleepTime = startingSleepTime
        set(value) {
            field = value
            cached = false
        }

    /**
     * Forces thread to sleep if [cached] is False.
     * Then sets [cached] flag to true and returns [sleepTime].
     * Else, just returns cached [sleepTime].
     */
    public val result: Number by lazyCached(
            {
                Thread.sleep(sleepTime)
                cached = true
                sleepTime
            },
            { !cached }
    )


}