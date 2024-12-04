package days

@Suppress("unused")
object Day4 : DayBase(4) {
    val matchMap = mapOf(
        'X' to 'M',
        'M' to 'A',
        'A' to 'S'
    )
    val oppositeDiagsMap = mapOf(
        DirectionTarget.TOP_LEFT to DirectionTarget.BOTTOM_RIGHT,
        DirectionTarget.BOTTOM_LEFT to DirectionTarget.TOP_RIGHT,
        DirectionTarget.TOP_RIGHT to DirectionTarget.BOTTOM_LEFT,
        DirectionTarget.BOTTOM_RIGHT to DirectionTarget.TOP_LEFT
    )

    enum class DirectionTarget(val x: Int, val y: Int) {
        TOP_LEFT(-1, -1),
        LEFT(-1, 0),
        BOTTOM_LEFT(-1, 1),
        BOTTOM(0, 1),
        BOTTOM_RIGHT(1, 1),
        RIGHT(1, 0),
        TOP_RIGHT(1, -1),
        TOP(0, -1)
    }

    fun <T : Comparable<T>> isValueAt(grid: Collection<Collection<T>>, x: Int, y: Int, value: T): Boolean {
        if (y < 0 || y > grid.size - 1) return false
        if (x < 0 || x > grid.elementAt(y).size - 1) return false
        return grid.elementAt(y).elementAt(x) == value
    }

    fun startLookup(
        grid: Collection<Collection<Char>>,
        x: Int, y: Int,
        char: Char = 'X',
        targetDirection: DirectionTarget? = null
    ): Int {
        val lookup = matchMap[char] ?: return 1
        var matches = 0
        for (direction in DirectionTarget.entries.let { targets ->
            if (targetDirection == null) targets else listOf(targetDirection)
        }) {
            val curX = x + direction.x
            val curY = y + direction.y
            if (isValueAt(grid, curX, curY, lookup)) {
                matches += startLookup(grid, curX, curY, lookup, direction)
            }
        }
        return matches
    }

    fun diagonalLookup(grid: Collection<Collection<Char>>, x: Int, y: Int): Boolean {
        var okDiags = 0
        val maxDiags = oppositeDiagsMap.size / 2
        for ((first, second) in oppositeDiagsMap.entries) {
            if (isValueAt(grid, x + first.x, y + first.y, 'M') &&
                isValueAt(grid, x + second.x, y + second.y, 'S')
            ) {
                okDiags++
                if (okDiags == maxDiags) return true
            }
        }
        return false
    }

    override fun solve(input: List<String>) {
        var sum = 0
        val collInput = input.map { it.toList() }
        for ((i, line) in collInput.withIndex()) {
            for ((j, char) in line.withIndex()) {
                if (char == 'X') {
                    sum += startLookup(collInput, j, i)
                }
            }
        }

        setPart1Answer(sum)

        sum = 0
        for ((i, line) in input.withIndex()) {
            if (i == 0 || i == input.size - 1) continue
            for ((j, char) in line.withIndex()) {
                if (j == 0 || j == line.length - 1) continue
                if (char == 'A' && diagonalLookup(collInput, j, i)) {
                    sum++
                }
            }
        }

        setPart2Answer(sum)
    }
}
