package days

@Suppress("unused")
object Day3 : DayBase(3) {
    val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")
    val doRegex = Regex("do\\(\\)")
    val dontRegex = Regex("don't\\(\\)")

    fun isRangeInList(ranges: List<IntRange>, range: IntRange): Boolean {
        for (r in ranges) {
            if (r.endInclusive < range.start) {
                continue
            }
            return r.endInclusive >= range.endInclusive && r.start <= range.start
        }
        return false
    }

    override fun solve(input: List<String>) {
        var sum = 0
        for (line in input) {
            for (match in mulRegex.findAll(line)) {
                val (mul1, mul2) = match.destructured
                sum += mul1.toInt() * mul2.toInt()
            }
        }

        setPart1Answer(sum)

        sum = 0
        val fullInput = input.joinToString("")
        val doEnds = doRegex.findAll(fullInput).toList().map { it.range.endInclusive }
        val dontStarts = dontRegex.findAll(fullInput).toList().map { it.range.start }
        val disallowedRanges = mutableListOf<IntRange>()
        var currentRange = IntRange(0, fullInput.length)

        for (index in (doEnds + dontStarts).sorted()) {
            if (index in dontStarts) { // create the first part of the range
                // if we're at the first "don't" that's greater than the last "do",
                // range till the end and leave; we're not going further
                if (doEnds.last() < index) {
                    disallowedRanges.add(IntRange(index, fullInput.length))
                    break
                }
                // only increment the first range item if we have populated
                // the second one to take the largest possible range
                if (currentRange.last > 0) {
                    currentRange = IntRange(index, 0)
                }
            } else if (index in doEnds && currentRange.start > 0) { // populate the second part of the range
                val newRange = IntRange(currentRange.start, index)
                currentRange = newRange
                // don't add a range starting by the same previous value
                if (disallowedRanges.lastOrNull()?.start == currentRange.start) {
                    continue
                }
                disallowedRanges.add(newRange)
            }
        }

        for (match in mulRegex.findAll(fullInput)) {
            val isBeforeFirstDont = match.range.start < dontStarts.first()
            if (isBeforeFirstDont || !isRangeInList(disallowedRanges, match.range)) {
                val (mul1, mul2) = match.destructured
                sum += mul1.toInt() * mul2.toInt()
            }
        }

        setPart2Answer(sum)
    }
}
