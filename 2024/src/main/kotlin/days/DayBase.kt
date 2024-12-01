package days

import kotlin.io.path.Path
import kotlin.io.path.readText

sealed class DayBase(val day: Int) {
    companion object {
        const val CURRENT_YEAR = 2024
        fun getAllDays() = DayBase::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
    }

    var _part1Answer: Int? = null
    var _part2Answer: Int? = null

    fun readInput() = Path("$CURRENT_YEAR/inputs/input$day.txt")
        .readText()
        .trim()
        .lines()

    abstract fun solve(input: List<String>)

    fun setPart1Answer(answer: Int) {
        _part1Answer = answer
    }

    fun setPart2Answer(result: Int) {
        _part2Answer = result
    }
}
