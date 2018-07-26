package examples

import lazyCached
import kotlin.reflect.KProperty

/**
 * Class, that represents any calculation with lazy invocation and caching of result, until parameters,
 * that are needed for [calculate] and [result], are changed
 */
public abstract class Calculable<T> {

    /**
     * Flag for caching
     */
    protected var cached = false

    /**
     * Getter for result calculation, that supports lazy invocation and caching of result
     * @return calculation result
     */
    public val result: T by lazyCached({ calculate().also { cached = true } }, { !cached })

    /**
     * Returns result of calculation
     * @return calculation result
     */
    protected abstract fun calculate(): T

    /**
     * Delegate for getters and setters for flagging [cached] as `false` if new value was setted
     */
    class Cacheable<T>(private var initial: T) {

        /**
         * @param calculable the object for which the value is requested (not used).
         * @param property the metadata for the property, used to get the name of property
         * and lookup the value corresponding to this name in the map.
         */
        operator fun getValue(calculable: Calculable<T>, property: KProperty<*>): T {
            return initial
        }

        /**
         * Sets value and flags, that cached result should not be used
         * @param calculable the object for which the value is requested
         * @param property the metadata for the property, used to get the name of property
         * and store the value associated with that name in the map.
         * @param new new value to be set.
         */
        operator fun setValue(calculable: Calculable<T>, property: KProperty<*>, new: T ) {
            if (initial != new) {
                calculable.cached = false
                initial = new
            }
        }

    }

}
