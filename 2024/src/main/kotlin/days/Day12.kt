package days

@Suppress("unused")
object Day12 : DayBase(12) {
    data class Position(val x: Int, val y: Int) {
        operator fun plus(direction: Direction) = Position(x + direction.dx, y + direction.dy)
    }

    data class Region(val letter: Char, var area: Int = 0) {
        val borderedPlots = mutableMapOf<Position, MutableSet<Direction>>()
    }

    enum class Direction(val dx: Int, val dy: Int) {
        NONE(0, 0),
        TOP(0, -1),
        BOTTOM(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        companion object {
            val all = Direction.entries.filter { it != NONE }
        }
    }

    val visitedAreaPositions = mutableMapOf<Char, MutableSet<Position>>()
    val regions = mutableListOf<Region>()

    fun visitPlot(
        currentRegion: Region,
        map: Collection<Collection<Char>>,
        originalPosition: Position,
        direction: Direction = Direction.NONE
    ) {
        val position = originalPosition + direction
        // Return conditions
        if (map.elementAtOrNull(position.y)?.elementAtOrNull(position.x) != currentRegion.letter) {
            // Null (out of bounds) or a different letter both mean an additional perimeter
            currentRegion.borderedPlots.computeIfAbsent(originalPosition) { mutableSetOf() }.add(direction)
            return
        }
        if (visitedAreaPositions[currentRegion.letter]?.contains(position) == true) return
        // Increase the area
        currentRegion.area++
        // Add the plot to the visited positions
        visitedAreaPositions.computeIfAbsent(currentRegion.letter) { mutableSetOf() }.add(position)
        // Finally, visit recursively
        for (direction in Direction.all) {
            visitPlot(currentRegion, map, position, direction)
        }
    }

    override fun solve(input: List<String>) {
        val charInput = input.map(String::toList)
        for ((j, line) in input.withIndex()) {
            for ((i, char) in line.withIndex()) {
                val pos = Position(i, j)
                if (visitedAreaPositions[char]?.contains(pos) == true) continue
                val region = Region(char)
                visitPlot(region, charInput, pos)
                regions.add(region)
            }
        }

        setPart1Answer(regions.fold(0) { acc, region -> acc + region.area * region.borderedPlots.values.flatten().size })
    }
}
