package days

import kotlin.math.abs

@Suppress("unused")
object Day2 : DayBase(2) {

    fun computePart2(input: List<String>, onBadLine: (String) -> Unit = {}): Int {
        var safeCount = 0
        for (line in input) {
            var previous: Int? = null
            var sign: Int? = null
            var badReports = 0
            for (level in line.split(" ").map { it.toInt() }) {
                if (previous == null) {
                    previous = level
                    continue
                }
                val diff = abs(level - previous)
                if (diff < 1 || diff > 3) {
                    // unsafe
                    badReports++
                    continue
                }
                if (sign == null) {
                    sign = level.compareTo(previous)
                    previous = level
                    continue
                } else if (sign != level.compareTo(previous)) {
                    // unsafe
                    badReports++
                    continue
                }
                previous = level
            }
            if (badReports < 2) {
                safeCount++
                continue
            }
            onBadLine(line)
        }

        return safeCount
    }

    override fun solve(input: List<String>) {
        var safeCount = 0
        main@ for (line in input) {
            var previous: Int? = null
            var sign: Int? = null
            for (level in line.split(" ").map { it.toInt() }) {
                if (previous == null) {
                    previous = level
                    continue
                }
                val diff = abs(level - previous)
                if (diff < 1 || diff > 3) {
                    // unsafe
                    continue@main
                }
                if (sign == null) {
                    sign = level.compareTo(previous)
                    previous = level
                    continue
                } else if (sign != level.compareTo(previous)) {
                    // unsafe
                    continue@main
                }
                previous = level
            }
            safeCount++
        }

        setPart1Answer(safeCount)

        val toRetry = mutableListOf<String>()
        safeCount = computePart2(input) { line ->
            line.split(" ").reversed().joinToString(" ").let {
                toRetry.add(it)
            }
        } + computePart2(toRetry)

        setPart2Answer(safeCount)
    }
}
