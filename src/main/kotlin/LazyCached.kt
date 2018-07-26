import java.io.Serializable

/**
 * Creates a new instance of the [LazyCached] that uses the specified initialization function [initializer]
 * and function [shouldRevoke], that contains behavior, that shows when [initializer] should be called,
 * if value is not null  and the default thread-safety mode [LazyThreadSafetyMode.SYNCHRONIZED].
 *
 * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
 *
 * Note that the returned instance uses itself to synchronize on. Do not synchronize from external code on
 * the returned instance as it may cause accidental deadlock. Also this behavior can be changed in the future.
 *
 * @param initializer should contain behavior for property calculation
 * @param shouldRevoke contains behavior, that shows when [initializer] should be called, if value is not null
 */

public fun <T> lazyCached(initializer: () -> T, shouldRevoke: () -> Boolean): Lazy<T> =
        LazyCached(initializer, shouldRevoke)

/**
 *
 * To create an instance of [LazyCached] use the [lazyCached] function.
 *
 * @param initializer should contain behavior for property calculation
 * @param shouldRevoke contains behavior, that shows when [initializer] should be called, if value is not null
 * @param lock lock for synchronization for concurrency
 */
private class LazyCached<out T>(val initializer: () -> T,
                                val shouldRevoke: () -> Boolean,
                                lock: Any? = null
): Lazy<T>, Serializable {
    @Volatile private var _value: Any? = null
    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    /**
     * Gets the lazily initialized value of the current Lazy instance.
     * Once the value was initialized it must not change during the rest of lifetime of this Lazy instance.
     */
    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== null && !shouldRevoke()) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== null && !shouldRevoke()) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    val typedValue = initializer()
                    _value = typedValue
                    typedValue
                }
            }
        }

    /**
     * Returns `true` if a value for this Lazy instance has been already initialized and if [shouldRevoke] is `false`,
     * and `false` otherwise.
     * Once this function has returned `true` it stays `true` until [shouldRevoke] is `true`.
     */
    override fun isInitialized() = synchronized(lock) {
        if (shouldRevoke()) {
            _value = null
        }
        _value !== null
    }

}