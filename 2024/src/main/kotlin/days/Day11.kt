package days

@Suppress("unused")
object Day11 : DayBase(11) {
    fun Long.toDigits(base: Int = 10) = sequence {
        var n = this@toDigits
        require(n >= 0)
        if (n == 0L) yield(n)
        while (n != 0L) {
            yield(n % base)
            n /= base
        }
    }.toList().reversed()

    fun processStone(stone: Long): List<Long> =
        if (stone == 0L) {
            // 0 becomes 1
            listOf(1)
        } else {
            val digits = stone.toDigits()
            if (digits.size % 2 == 0) {
                // Even number of digits → split in 2
                digits.chunked(digits.size / 2).map { it.joinToString("").toLong() }
            } else {
                // Otherwise, multiply by 2024
                listOf(stone * CURRENT_YEAR)
            }
        }

    fun processStones(m: Map<Long, Int>): Map<Long, Int> {
        val n = mutableMapOf<Long, Int>()
        for ((key, value) in m) {
            for (stone in processStone(key)) {
                n[stone] = n[stone]?.let { it + value } ?: value
            }
        }
        return n
    }

    override fun solve(input: List<String>) {
        // Parsing
        val numbers = input.first().split(" ").map(String::toLong)

        var map = numbers.groupingBy { it }.eachCount()

        // Part 1
        repeat(25) {
            map = processStones(map)
        }

        setPart1Answer(map.values.sum())
    }
}
