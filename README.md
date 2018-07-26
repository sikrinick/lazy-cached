# LazyCached
Simple library, that adds possibility of caching to Kotlin's `lazy`.

#### [Check examples](#Example)  
#### [Gradle](#get-into-gradle)
#### [Maven](#get-into-maven)


### Example
For example, imagine we have some resource-intensive operation:
```
import lazyCached

/**
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
```
In this case, on changing `sleepTime` it is required to recalculate `result`, but it is better to do it in a lazy way.

This problem is solved with `lazyCached`.  
* First argument of `lazyCached` delegate is same as for `lazy`.   
* Second argument is some condition, that returns `True` or `False`, 
where `True` forces first argument to be revoked again.  
* Third argument is same `lock` as for `lazy`.

[More examples](src/main/kotlin/examples)  
[Tests](src/test/kotlin)


### Imports
#### Get into Gradle
Add it in your root `build.gradle` at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```
dependencies {
    implementation 'com.github.sikrinick:lazy-cached:v1.0.0'
}
```
#### Get into Maven
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add the dependency
```
<dependency>
    <groupId>com.github.sikrinick</groupId>
    <artifactId>lazy-cached</artifactId>
    <version>v1.0.0</version>
</dependency>
```