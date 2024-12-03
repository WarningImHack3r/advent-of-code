import days.DayBase

enum class Colors {
    RESET,
    BLUE,
    CYAN;

    override fun toString() = when (this) {
        RESET -> "\u001b[0m"
        BLUE -> "\u001b[34m"
        CYAN -> "\u001b[36m"
    }
}

fun printAnswer(day: Int, part: Int, answer: Int) {
    val formattedOutput = "%,d".format(answer)
    val output = if (formattedOutput != "$answer") " ($formattedOutput)" else ""
    println("${Colors.BLUE}[Day $day]${Colors.CYAN} Part $part:${Colors.RESET} $answer$output")
}

fun main() {
    val d = DayBase.getAllDays().maxByOrNull { it.day } ?: run {
        println("No implementation found. Implement the DayBase class to begin!")
        return
    }
    d.solve(d.readInput())

    if (d._part1Answer != null || d._part2Answer != null) println()
    d._part1Answer?.let { printAnswer(d.day, 1, it) }
    d._part2Answer?.let { printAnswer(d.day, 2, it) }
}
