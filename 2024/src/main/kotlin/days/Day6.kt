package days


@Suppress("unused")
object Day6 : DayBase(6) {
    /**
     * The guard instance
     */
    lateinit var guard: Guard

    data class Guard(
        val initialPositionX: Int,
        val initialPositionY: Int,
        var positionX: Int,
        var positionY: Int,
        var direction: Direction
    ) {
        enum class Direction(val x: Int, val y: Int) {
            TOP(0, -1),
            BOTTOM(0, 1),
            LEFT(-1, 0),
            RIGHT(1, 0);

            companion object {
                fun fromChar(char: Char) = when (char) {
                    '^' -> TOP
                    'v' -> BOTTOM
                    '<' -> LEFT
                    '>' -> RIGHT
                    else -> throw IllegalArgumentException("Invalid direction: $char")
                }

            }

            // These names look like history entries but are not, it just looks good like that
            fun previous() = when (this) {
                TOP -> LEFT
                LEFT -> BOTTOM
                BOTTOM -> RIGHT
                RIGHT -> TOP
            }

            fun next() = when (this) {
                TOP -> RIGHT
                RIGHT -> BOTTOM
                BOTTOM -> LEFT
                LEFT -> TOP
            }
        }
    }

    override fun solve(input: List<String>) {
        // Find the guard's position and direction
        grid@ for ((j, line) in input.withIndex()) {
            for ((i, char) in line.withIndex()) {
                if (char == '#' || char == '.') continue
                guard = Guard(
                    i, j, i, j,
                    Guard.Direction.fromChar(char),
                )
                break@grid
            }
        }

        // Rotate to the previous direction to not rotate twice in the first loop
        guard.direction = guard.direction.previous()

        // Move the guard
        val visitedPositions = mutableSetOf<Pair<Int, Int>>()
        while (guard.positionX > 0 && guard.positionX < input[0].length - 1
            && guard.positionY > 0 && guard.positionY < input.size - 1
        ) {
            guard.direction = guard.direction.next()

            when (guard.direction) {
                Guard.Direction.TOP -> {
                    for (pos in guard.positionY - 1 downTo 0) {
                        val charAt = input[pos][guard.positionX]
                        guard.positionY = if (charAt == '#') pos + 1 else pos
                        if (charAt != '#') { // '.' or ^/v/</>
                            visitedPositions.add(Pair(guard.positionX, pos))
                            if (pos > 0) continue
                        }
                        break
                    }
                }

                Guard.Direction.BOTTOM -> {
                    for (pos in guard.positionY + 1 until input.size) {
                        val charAt = input[pos][guard.positionX]
                        guard.positionY = if (charAt == '#') pos - 1 else pos
                        if (charAt != '#') {
                            visitedPositions.add(Pair(guard.positionX, pos))
                            if (pos < input.size - 1) continue
                        }
                        break
                    }
                }

                Guard.Direction.LEFT -> {
                    for (pos in guard.positionX - 1 downTo 0) {
                        val charAt = input[guard.positionY][pos]
                        guard.positionX = if (charAt == '#') pos + 1 else pos
                        if (charAt != '#') {
                            visitedPositions.add(Pair(pos, guard.positionY))
                            if (pos > 0) continue
                        }
                        break
                    }
                }

                Guard.Direction.RIGHT -> {
                    for (pos in guard.positionX + 1 until input[guard.positionY].length) {
                        val charAt = input[guard.positionY][pos]
                        guard.positionX = if (charAt == '#') pos - 1 else pos
                        if (charAt != '#') {
                            visitedPositions.add(Pair(pos, guard.positionY))
                            if (pos < input[guard.positionY].length - 1) continue
                        }
                        break
                    }
                }
            }
        }

        setPart1Answer(visitedPositions.size)
    }
}
