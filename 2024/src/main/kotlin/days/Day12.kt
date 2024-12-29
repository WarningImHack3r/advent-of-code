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

        fun opposite() = when (this) {
            TOP -> BOTTOM
            BOTTOM -> TOP
            LEFT -> RIGHT
            RIGHT -> LEFT
            NONE -> NONE
        }

        fun normals() = when (this) {
            TOP, BOTTOM -> setOf(LEFT, RIGHT)
            LEFT, RIGHT -> setOf(TOP, BOTTOM)
            NONE -> emptySet()
        }

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

    fun computeSides(region: Region, map: Collection<Collection<Char>>): Int {
        // For that one, I initially wanted to "merge"
        // the borders that are close to each other and
        // in the same direction, but although the idea
        // looks great, it's an absolute nightmare to
        // implement from what I've tried.
        // Instead, @Milhouzer gave me the intel that,
        // in almost all cases, sides == angles.
        // So, let's count angles instead!
        var angles = 0
        val outerChecked = mutableMapOf<Position, MutableSet<Position>>()

        for ((plot, borders) in region.borderedPlots) {
            // Count inner angles
            when (borders.size) {
                2 -> if (borders.none { it.opposite() in borders }) {
                    // One single angle if the borders are not opposites
                    angles++
                }

                // I know I told you I wouldn't play with sides anymore
                3 -> angles += borders.size - 1
                4 -> angles += borders.size

                // 0 or 1 are irrelevant here
            }

            // Count outer angles
            // It works by checking whether an outer plot is
            // diagonal to a plot of the region, and adds an
            // angle if so
            for (border in borders) {
                // for each existing border, find its non-border normals
                val nonBorderNormals = border.normals().filter { it !in borders }
                for (nonBorderNormal in nonBorderNormals) {
                    // for each non-border normal, check if the plot in that direction,
                    // then in the same direction as the border has a border OPPOSITE to
                    // the non-border normal one (yeah, read that once again slowly).
                    // the latter border and the `border` then both create an outer angle.
                    // we also know that the plot + non-border normal is part of the region
                    // because otherwise there would have been a border for that normal.
                    if (region.borderedPlots[plot + nonBorderNormal + border]?.contains(nonBorderNormal.opposite()) == true) {
                        // as we're "side-agnostic", we have to check if that corner did not already
                        // check for an outer angle
                        if (outerChecked[plot + border]?.contains(plot + nonBorderNormal) == true) continue
                        // if everything's good, increment the angles and prevent rechecking it :)
                        angles++
                        outerChecked.computeIfAbsent(plot + border) { mutableSetOf() }.add(plot + nonBorderNormal)
                    }
                }
            }
        }

        return angles
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
        setPart2Answer(regions.fold(0) { acc, region -> acc + region.area * computeSides(region, charInput) })
    }
}
