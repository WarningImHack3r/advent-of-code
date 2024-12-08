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

fun printAnswer(day: Int, part: Int, answer: Int) {
    val formattedOutput = "%,d".format(answer)
    val output = if (formattedOutput != "$answer") "\t($formattedOutput)" else ""
    println("${Colors.BLUE}[Day $day]${Colors.CYAN} Part $part:${Colors.RESET} $answer$output")
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
    val execTime = measureTime {
        d.solve(d.readInput())
    }

    if (d._part1Answer != null || d._part2Answer != null) println()
    d._part1Answer?.let { printAnswer(d.day, 1, it) }
    d._part2Answer?.let { printAnswer(d.day, 2, it) }
    printExecutionTime(execTime)
}
