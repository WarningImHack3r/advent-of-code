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

    override fun solve(input: List<String>) {
        // Parsing
        for (line in input) {
            val (total, numbers) = line.split(":").map(String::trim)
            val numbersList = numbers.split(" ").map(String::toLong)
            ops.add(Operation(total.toLong(), numbersList))
        }

        // Part 1
        setPart1Answer(ops.filter { compute(it.total, 0, it.numbers) }.sumOf { it.total })
    }
}
