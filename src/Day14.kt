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

fun lineToRobot(line: String): Robot {
    return Robot(
        point = Point(line.split(" ")[0].removePrefix("p=")),
        velocity = Point(line.split(" ")[1].removePrefix("v="))
    )
}

fun main() {
    fun part1(): Int {
        val map = Map(Point(101, 103), readInput("Day14_test").map { lineToRobot(it) })


        repeat(100) {
            map.tick()
        }

        return map.getQuadrants().fold(1) { factor, quadrant ->
            factor * map.getSubMap(quadrant.first, quadrant.second).robots.size
        }
    }

    fun part2(): Int {
        val map = Map(Point(101, 103), readInput("Day14_test").map { lineToRobot(it) })

        var tick = 0
        while (true) {
            tick += 1
            map.tick()

            // Basic entropy detection
            var neighborCount = 0
            map.robots.forEach { robot ->
                map.robots.forEach { otherRobot ->
                    if (robot != otherRobot && robot.point.manhattanDistance(otherRobot.point) <= 2) {
                        neighborCount += 1
                    }
                }
            }
            //println("tick $tick has $neighborCount neighbors")
            if (neighborCount > 1000) { // 1000 feels good enough, first ticks have ~300 neighbors
                println(map)
                println("tick $tick has $neighborCount neighbors")
                return tick
            }
        }
    }

    part1().println()
    part2().println()
}
