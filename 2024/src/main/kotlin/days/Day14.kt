package days

@Suppress("unused")
object Day14 : DayBase(14) {
    data class Position(val x: Int, val y: Int)
    data class Robot(val position: Position, val rightTps: Int, val downTps: Int)

    val robots = mutableListOf<Robot>()

    const val GRID_WIDTH = 101
    const val GRID_HEIGHT = 103
    const val EXAMPLE_GRID_WIDTH = 11
    const val EXAMPLE_GRID_HEIGHT = 7

    fun christmasTreeDetector(robotsState: Collection<Robot>, gridWidth: Int, gridHeight: Int, maxSeconds: Int): Int {
        // I've seen the tree online a few times, so I know what it looks like
        // and that there is a border around it.
        // However, I don't know the size of neither the tree nor the border.
        // As a result, we'll try to guess we found it by trying to find a shape of a
        // border, and actually print it when we think we found it! :P

        val targetWidth = 20
        val robotsPositions = robotsState.map { it.position }
        for (s in 1..maxSeconds) {
            val movedRobots = robotsPositions.mapIndexed { i, position ->
                robotPositionAtSecond(robotsState.elementAt(i), s, gridWidth, gridHeight)
            }

            for (i in 0 until gridHeight) {
                val robotsInRow = movedRobots.filter { pos -> pos.y == i }
                if (getConsecutiveNumbers(robotsInRow.map { pos -> pos.x }.sorted()).any { suite ->
                        suite.size >= targetWidth
                    }
                ) {
                    printGrid(movedRobots, gridWidth, gridHeight)
                    return s
                }
            }
        }

        return 0
    }

    /**
     * Return a list of lists of numbers with an exact step of 1
     * between each.
     * The input list must be sorted.
     *
     * @param srcList the input list
     * @return the list of lists of consecutive numbers
     *
     * @see <a href="https://stackoverflow.com/a/62257243/12070367">StackOverflow</a>
     */
    fun getConsecutiveNumbers(srcList: List<Int>): List<List<Int>> {
        return srcList.fold(mutableListOf<MutableList<Int>>()) { acc, i ->
            if (acc.isEmpty() || acc.last().last() != i - 1) {
                acc.add(mutableListOf(i))
            } else acc.last().add(i)
            acc
        }
    }

    fun printGrid(
        robotPositions: Collection<Position>,
        gridWidth: Int,
        gridHeight: Int,
        debug: Boolean = false
    ) {
        if (debug) {
            print("\u001b[H\u001b[2J")
            System.out.flush()
        }
        for (j in 0 until gridHeight) {
            for (i in 0 until gridWidth) {
                print(if (Position(i, j) in robotPositions) '#' else '.')
            }
            println()
        }
        if (debug) readln()
    }

    fun robotPositionAtSecond(robot: Robot, second: Int, gridWidth: Int, gridHeight: Int): Position {
        return Position(
            (robot.position.x + robot.rightTps * second).mod(gridWidth),
            (robot.position.y + robot.downTps * second).mod(gridHeight)
        )
    }

    override fun solve(input: List<String>) {
        // Parsing
        for (line in input) {
            val (position, velocities) = line.split(" ")
            val (px, py) = position.substringAfter("p=").split(",").map(String::toInt)
            val (vx, vy) = velocities.substringAfter("v=").split(",").map(String::toInt)
            robots.add(Robot(Position(px, py), vx, vy))
        }

        // Adjust grid size
        val isExample = robots.size < 20 // good enough value to differentiate example from real dataset
        val gridWidth = if (isExample) EXAMPLE_GRID_WIDTH else GRID_WIDTH
        val gridHeight = if (isExample) EXAMPLE_GRID_HEIGHT else GRID_HEIGHT

        // Part 1
        val seconds = 100
        val movedRobots = robots.map { robot ->
            robot.copy(position = robotPositionAtSecond(robot, seconds, gridWidth, gridHeight))
        }

        val topLeftQuadrant = movedRobots.filter { robot ->
            robot.position.x < gridWidth / 2 && robot.position.y < gridHeight / 2
        }.size
        val topRightQuadrant = movedRobots.filter { robot ->
            robot.position.x > gridWidth / 2 && robot.position.y < gridHeight / 2
        }.size
        val bottomLeftQuadrant = movedRobots.filter { robot ->
            robot.position.x < gridWidth / 2 && robot.position.y > gridHeight / 2
        }.size
        val bottomRightQuadrant = movedRobots.filter { robot ->
            robot.position.x > gridWidth / 2 && robot.position.y > gridHeight / 2
        }.size

        setPart1Answer(topLeftQuadrant * topRightQuadrant * bottomLeftQuadrant * bottomRightQuadrant)

        // Part 2
        if (!isExample) {
            setPart2Answer(christmasTreeDetector(robots, gridWidth, gridHeight, 10_000))
        }
    }
}
