import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun assert(expected: Any, actual: Any) = check(expected.toString() == actual.toString()) { "Expected $expected but was $actual" }

open class Point(val x: Long, val y: Long) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun times(factor: Long) = Point(x * factor, y * factor)
    fun manhattanDistance(other: Point) = (x - other.x).absoluteValue + (y - other.y).absoluteValue
    fun modulus(other: Point) = Point(
        (x % other.x).let { if (it < 0) it + other.x else it },
        (y % other.y).let { if (it < 0) it + other.y else it }
    )

    override fun toString() = "($x, $y)"
    infix fun positionEquals(other: Point) = x == other.x && y == other.y

    constructor(input: String) : this(input.split(",")[0].toLong(), input.split(",")[1].toLong())
}