package days

@Suppress("unused")
object Day5 : DayBase(5) {
    val orderingMap = mutableMapOf<Int, MutableList<Int>>()
    val sequences = mutableListOf<List<Int>>()

    override fun solve(input: List<String>) {
        // Parsing
        var isParsingSequences = false
        for (line in input) {
            if (line.isEmpty()) {
                isParsingSequences = true
                continue
            }

            if (isParsingSequences) {
                sequences.add(line.split(",").map(String::toInt))
            } else {
                val (base, beforePage) = line.split("|").map(String::toInt)
                orderingMap.computeIfAbsent(base) { mutableListOf() }.add(beforePage)
            }
        }

        val incorrectSequences = mutableListOf<List<Int>>()

        var sum = 0
        sequences@ for (line in sequences) {
            for ((i, digit) in line.withIndex()) {
                val afterPages = orderingMap[digit] ?: emptyList()
                if (afterPages.isEmpty()) continue
                for (after in line.subList(0, i)) {
                    if (after in afterPages) {
                        // ChatGPT helped me to translate my thoughts into this `sortedWith`,
                        // sorry not sorry lol
                        incorrectSequences.add(line.sortedWith { a, b ->
                            when {
                                orderingMap[a]?.contains(b) == true -> -1
                                orderingMap[b]?.contains(a) == true -> 1
                                else -> 0
                            }
                        })
                        continue@sequences
                    }
                }
            }
            sum += line[line.size / 2]
        }

        setPart1Answer(sum)

        sum = 0
        for (line in incorrectSequences) {
            sum += line[line.size / 2]
        }

        setPart2Answer(sum)
    }
}
