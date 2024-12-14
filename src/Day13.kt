import kotlin.math.max
import kotlin.math.min

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

            val baseLowCostButtonCount = min(price.x / lowCostButton.x, price.y / lowCostButton.y)

            for (tick in baseLowCostButtonCount downTo max(0L, baseLowCostButtonCount - 1000)) {
                var currentTokens = lowCostButton.cost * tick
                var target = lowCostButton * tick

                for (highCostButtonPresses in 0..1000) {
                    currentTokens += highCostButton.cost
                    target += highCostButton
                    if (target.x >= price.x || target.y >= price.y) {
                        break
                    }
                }

                if (target.x == price.x && target.y == price.y) {
                    totalTokens += currentTokens
                    return@forEach
                }
            }
        }
        return totalTokens
    }

    fun part1(): Long {
        return calculateTokens(readInput("Day13_test").toMachines())
            .also { check(it == 36250L, { "total tokens $it" }) }
    }

    fun part2(input: List<String>): Long {
        return calculateTokens(
            readInput("Day13_test").toMachines()
                .onEach { it.price = Price(it.price.x + 10000000000000L, it.price.y + 10000000000000L) })
    }
    part1().println()
    //part2(input).println()
}
