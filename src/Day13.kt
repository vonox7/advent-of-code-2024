class Button(x: Long, y: Long, val cost: Long) : Point(x, y)
class Price(x: Long, y: Long) : Point(x, y)
data class Machine(val buttons: List<Button>, var price: Price)

fun List<String>.toMachines(): List<Machine> {
    return this.windowed(3, step = 4) { (a, b, price, _) ->
        Machine(
            buttons = listOf(
                Button(
                    a.substringAfter("X+").substringBefore(",").toLong(),
                    a.substringAfter("Y+").toLong(),
                    cost = 3
                ),
                Button(
                    b.substringAfter("X+").substringBefore(",").toLong(),
                    b.substringAfter("Y+").toLong(),
                    cost = 1
                )
            ),
            price = Price(
                price.substringAfter("X=").substringBefore(",").toLong(),
                price.substringAfter("Y=").toLong()
            )
        )
    }
}

fun main() {
    fun calculateTokens(machines: List<Machine>): Long {
        var totalTokens = 0L
        machines.forEach { (buttons, price) ->
            val lowCostButton = buttons.minBy { it.cost * (it.x + it.y) }
            val highCostButton = buttons.single { it != lowCostButton }

            // Cross the lines: https://de.wikipedia.org/wiki/Schnittpunkt
            // x1 = y1 = 0

            val x2 = lowCostButton.x
            val y2 = lowCostButton.y

            val x3 = price.x
            val y3 = price.y

            val x4 = price.x - highCostButton.x
            val y4 = price.y - highCostButton.y

            val denominator = (x2 * (y4 - y3) - y2 * (x4 - x3))
            if (denominator == 0L) {
                throw IllegalStateException("parallel button directions")
            }

            val s_x_times_denominator = (x2 * (x3 * y4 - y3 * x4))
            val s_y_times_denominator = (y2 * (x3 * y4 - y3 * x4))

            val s_x = s_x_times_denominator / denominator
            val s_y = s_y_times_denominator / denominator

            // Check if we had whole numbers
            if (s_x * denominator != s_x_times_denominator || s_y * denominator != s_y_times_denominator) {
                return@forEach
            }

            if (s_x < 0 || s_x > price.x || s_y < 0 || s_y > price.y) {
                return@forEach
            }

            val cost =
                lowCostButton.cost * (s_x / lowCostButton.x) + highCostButton.cost * (price.y - s_y) / highCostButton.y
            totalTokens += cost
        }
        return totalTokens
    }

    fun part1(): Long {
        return calculateTokens(readInput("Day13_test").toMachines())
            .also { check(it == 36250L, { "total tokens $it" }) }
    }

    fun part2(): Long {
        return calculateTokens(
            readInput("Day13_test").toMachines()
                .onEach { it.price = Price(it.price.x + 10000000000000L, it.price.y + 10000000000000L) })
            .also { check(it < 85132304518943L, { "total tokens $it" }) }

    }
    part1().println()
    part2().println()
}
