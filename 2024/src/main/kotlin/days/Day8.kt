package days

@Suppress("unused")
object Day8 : DayBase(8) {
    val antennas = mutableMapOf<Char, MutableList<Point>>()
    val antiNodes = mutableSetOf<Point>()

    data class Point(val x: Int, val y: Int)

    fun isPointInGrid(gridWidth: Int, gridHeight: Int, p: Point): Boolean {
        return p.x in 0 until gridWidth && p.y in 0 until gridHeight
    }

    fun computeAntiNodes(gridWidth: Int, gridHeight: Int, p1: Point, p2: Point) {
        // Get the distance between the two points
        val distanceX = p2.x - p1.x
        val distanceY = p2.y - p1.y

        // Extrapolate them to the other points
        val p3 = Point(p1.x - distanceX, p1.y - distanceY)
        val p4 = Point(p2.x + distanceX, p2.y + distanceY)

        // Panic if they're equal to one of the others
        if (p3 == p1 || p3 == p2 || p4 == p1 || p4 == p2) throw Exception("Computation lead to identical points")

        // Add them to the set if needed
        if (isPointInGrid(gridWidth, gridHeight, p3)) antiNodes.add(p3)
        if (isPointInGrid(gridWidth, gridHeight, p4)) antiNodes.add(p4)
    }

    fun computeFurtherAntiNodes(gridWidth: Int, gridHeight: Int, p1: Point, p2: Point) {
        val distanceX = p2.x - p1.x
        val distanceY = p2.y - p1.y

        var newPoint = Point(p1.x - distanceX * 2, p1.y - distanceY * 2)
        while (isPointInGrid(gridWidth, gridHeight, newPoint)) {
            antiNodes.add(newPoint)
            newPoint = Point(newPoint.x - distanceX, newPoint.y - distanceY)
        }

        newPoint = Point(p2.x + distanceX, p2.y + distanceY)
        while (isPointInGrid(gridWidth, gridHeight, newPoint)) {
            antiNodes.add(newPoint)
            newPoint = Point(newPoint.x + distanceX, newPoint.y + distanceY)
        }
    }

    override fun solve(input: List<String>) {
        // Parsing
        for ((i, line) in input.withIndex()) {
            for ((j, char) in line.withIndex()) {
                if (char == '.') continue
                antennas.computeIfAbsent(char) { mutableListOf() }.add(Point(i, j))
            }
        }

        // Part 1
        for ((_, v) in antennas) {
            for (p1 in v) {
                for (p2 in v) {
                    if (p1 == p2) continue
                    computeAntiNodes(input.first().length, input.size, p1, p2)
                }
            }
        }

        setPart1Answer(antiNodes.size)

        // Part 2
        for ((_, v) in antennas) {
            for (p1 in v) {
                for (p2 in v) {
                    if (p1 == p2) continue
                    computeFurtherAntiNodes(input.first().length, input.size, p1, p2)
                }
            }
        }

        setPart2Answer(antiNodes.size + antennas.values.flatten().toSet().filter { it !in antiNodes }.size)
    }
}
