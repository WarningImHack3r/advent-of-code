package days

import kotlin.math.abs

@Suppress("unused")
object Day1 : DayBase(1) {
    var leftList = listOf<Int>()
    var rightList = listOf<Int>()

    override fun solve(input: List<String>) {
        // Parsing
        for (line in input) {
            val (left, right) = line.split(Regex("\\s+"))
            leftList += left.toInt()
            rightList += right.toInt()
        }

        // Sorting
        leftList = leftList.sorted()
        rightList = rightList.sorted()

        // Part 1
        var sum = 0
        for ((index, left) in leftList.withIndex()) {
            sum += abs(left - rightList[index])
        }

        setPart1Answer(sum)

        // Part 2
        sum = 0
        for (left in leftList) {
            var occurrences = 0
            for (right in rightList) {
                if (left > right) {
                    continue
                }
                if (left == right) {
                    occurrences++
                    continue
                }
                break
            }
            sum += left * occurrences
        }

        setPart2Answer(sum)
    }
}
