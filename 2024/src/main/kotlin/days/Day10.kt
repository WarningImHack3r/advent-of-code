package days


@Suppress("unused")
object Day10 : DayBase(10) {
    data class Position(val x: Int, val y: Int)

    val trailheadsScores = mutableMapOf<Position, Int>()
    val stopsForTrailheads = mutableMapOf<Position, MutableSet<Position>>()

    /**
     * Starting at [startAt] in the map [input], look for [dest] by
     * searching in all 4 directions.
     * Once [dest] is found, call [onDestFound] with its position.
     *
     * @param input the input map
     * @param startAt the starting position — its value is NOT verified
     * @param dest the destination digit to look for
     * @param onDestFound the callback called when [dest] is found with its position
     * @param current an internal value to track the current lookup position
     * @param wants an internal value to incrementally look for the next value until [dest] is found
     */
    fun computePathsCount(
        input: Collection<Collection<Char>>,
        startAt: Position,
        dest: Char,
        onDestFound: (Position) -> Int = { 1 },
        current: Position? = null,
        wants: Char = '0'
    ): Int {
        val curr = current ?: startAt
        val elementAt = input.elementAtOrNull(curr.y)?.elementAtOrNull(curr.x) ?: return 0
        if (elementAt != wants) return 0
        if (elementAt == dest) return onDestFound(curr)
        return computePathsCount(input, startAt, dest, onDestFound, Position(curr.x + 1, curr.y), wants + 1) +
                computePathsCount(input, startAt, dest, onDestFound, Position(curr.x - 1, curr.y), wants + 1) +
                computePathsCount(input, startAt, dest, onDestFound, Position(curr.x, curr.y + 1), wants + 1) +
                computePathsCount(input, startAt, dest, onDestFound, Position(curr.x, curr.y - 1), wants + 1)
    }

    override fun solve(input: List<String>) {
        val charInput = input.map(String::toList)

        // Part 1
        // "Parsing": find trailheads
        for ((j, line) in charInput.withIndex()) {
            for ((i, char) in line.withIndex()) {
                if (char == '0') {
                    val pos = Position(i, j)
                    trailheadsScores[pos] = computePathsCount(charInput, pos, '9', { destPos ->
                        if (stopsForTrailheads.computeIfAbsent(pos) {
                                mutableSetOf()
                            }.contains(destPos)) {
                            return@computePathsCount 0
                        }
                        stopsForTrailheads[pos]?.add(destPos)
                        return@computePathsCount 1
                    })
                }
            }
        }

        setPart1Answer(trailheadsScores.values.sum())

        // Part 2
        trailheadsScores.clear()

        for ((j, line) in charInput.withIndex()) {
            for ((i, char) in line.withIndex()) {
                if (char == '0') {
                    val pos = Position(i, j)
                    trailheadsScores[pos] = computePathsCount(charInput, pos, '9')
                }
            }
        }

        setPart2Answer(trailheadsScores.values.sum())
    }
}
