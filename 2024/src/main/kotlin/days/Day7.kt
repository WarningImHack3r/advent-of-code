package days


@Suppress("unused")
object Day7 : DayBase(7) {
    val ops = mutableListOf<Operation>()

    data class Operation(val total: Long, val numbers: List<Long>)

    fun compute(expected: Long, current: Long, list: List<Long>): Boolean {
        val n = list.firstOrNull() ?: return expected == current
        val rest = list.drop(1)
        return compute(expected, current + n, rest) || compute(expected, current * n, rest)
    }

    fun computeWithOr(expected: Long, current: Long, list: List<Long>): Boolean {
        val n = list.firstOrNull() ?: return expected == current
        val rest = list.drop(1)
        return computeWithOr(expected, current + n, rest)
                || computeWithOr(expected, (if (current == 0L) 1 else current) * n, rest)
                || computeWithOr(expected, "${if (current == 0L) "" else current}$n".toLong(), rest)
    }

    override fun solve(input: List<String>) {
        // Parsing
        for (line in input) {
            val (total, numbers) = line.split(":").map(String::trim)
            val numbersList = numbers.split(" ").map(String::toLong)
            ops.add(Operation(total.toLong(), numbersList))
        }

        // Part 1
        setPart1Answer(ops.filter { compute(it.total, 0, it.numbers) }.sumOf { it.total })

        // Part 2
        setPart2Answer(ops.filter { computeWithOr(it.total, 0, it.numbers) }.sumOf { it.total })
    }
}
