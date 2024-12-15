import days.DayBase
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

enum class Colors {
    RESET, BOLD, RED, GREEN,
    BLUE,
    CYAN;

    override fun toString() = when (this) {
        RESET -> "\u001b[0m"
        BOLD -> "\u001b[1m"
        RED -> "\u001b[31m"
        GREEN -> "\u001b[32m"
        BLUE -> "\u001b[34m"
        CYAN -> "\u001b[36m"
    }
}

fun printAnswer(day: Int, part: Int, answer: Long, matchesExample: Boolean? = null) {
    val formattedOutput = "%,d".format(answer)
    val output = if (formattedOutput != "$answer") "\t($formattedOutput)" else ""
    val tick = when (matchesExample) {
        true -> " ✅"
        false -> " ❌"
        null -> ""
    }
    println("${Colors.BLUE}[Day $day]${Colors.CYAN} Part $part:${Colors.RESET} $answer$output$tick")
}

fun printExecutionTime(executionTime: Duration) {
    println(
        "\n${Colors.BOLD}Execution time: ${
            if (executionTime > 1.seconds) {
                Colors.RED
            } else {
                Colors.GREEN
            }
        }$executionTime${Colors.RESET}"
    )
}

fun main() {
    val d = DayBase.getAllDays().maxByOrNull { it.day } ?: run {
        println("No implementation found. Implement the DayBase class to begin!")
        return
    }

    // Compute example
    val example = Examples.get(d.day)
    if (!d.testMode) {
        example?.input?.let { d.solve(it) }
    }
    val d1example = d._part1Answer?.also { d._part1Answer = null }
    val d2example = d._part2Answer?.also { d._part2Answer = null }

    // Compute the real input
    val input = if (d.testMode) {
        example?.input ?: throw Exception("No example input despite test mode enabled")
    } else d.readInput()
    val execTime = measureTime { d.solve(input) }

    if (d._part1Answer != null || d._part2Answer != null) println()
    d._part1Answer?.let { ans ->
        printAnswer(
            d.day,
            1,
            ans,
            example?.part1Answer?.let { p1 ->
                (if (d.testMode) ans else d1example)?.let { it == p1 }
            }
        )
    }
    d._part2Answer?.let { ans ->
        printAnswer(
            d.day,
            2,
            ans,
            example?.part2Answer?.let { p2 ->
                (if (d.testMode) ans else d2example)?.let { it == p2 }
            }
        )
    }
    printExecutionTime(execTime)
}
