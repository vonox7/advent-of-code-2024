data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    fun modulus(other: Point) = Point(
        (x % other.x).let { if (it < 0) it + other.x else it },
        (y % other.y).let { if (it < 0) it + other.y else it }
    )
    override fun toString() = "($x, $y)"

    constructor(input: String) : this(input.split(",")[0].toInt(), input.split(",")[1].toInt())
}

data class Robot(var point: Point, val velocity: Point)
class Map(val size: Point, val robots: List<Robot>) {
    override fun toString() = buildString {
        for (y in 0 until size.y) {
            for (x in 0 until size.x) {
                val robotCount = robots.count { it.point == Point(x, y) }
                if (robotCount > 0) {
                    append(robotCount)
                } else {
                    append('.')
                }
            }
            appendLine()
        }
    }

    fun tick() {
        robots.forEach { robot ->
            robot.point = (robot.point + robot.velocity).modulus(size)
        }
    }

    fun getSubMap(startPoint: Point, endPointExclusive: Point) = Map(
        size = endPointExclusive - startPoint,
        robots = this.robots.filter { robot ->
            robot.point.x >= startPoint.x && robot.point.x < endPointExclusive.x &&
                robot.point.y >= startPoint.y && robot.point.y < endPointExclusive.y
        }.map { it.copy(point = it.point - startPoint) }
    )

    fun getQuadrants(): List<Pair<Point, Point>> = listOf(
        Point(0, 0) to Point((size.x / 2), (size.y / 2)),
        Point((size.x / 2) + 1, 0) to Point(size.x, (size.y / 2)),
        Point(0, (size.y / 2) + 1) to Point((size.x / 2), size.y),
        Point((size.x / 2) + 1, (size.y / 2) + 1) to Point(size.x, size.y),
    )
}

fun main() {
    val map = Map(Point(101, 103), readInput("Day14_test").map { line ->
        val sections = line.split(" ")
        Robot(point=Point(sections[0].removePrefix("p=")), velocity=Point(sections[1].removePrefix("v=")))
    })
    println(map)

    fun part1(): Int {
        repeat(100) {
            map.tick()
        }

        return map.getQuadrants().fold(1) { factor, quadrant ->
            factor * map.getSubMap(quadrant.first, quadrant.second).robots.size
        }
    }

    fun part2(): Int {
        return 0
    }

    part1().println()
    part2().println()
}
