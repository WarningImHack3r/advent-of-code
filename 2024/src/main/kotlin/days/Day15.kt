package days

import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
object Day15 : DayBase(15) {
    interface Point {
        val x: Int
        val y: Int

        operator fun plus(direction: Direction): Point
    }

    interface MoveableObject : Point {
        fun move(direction: Direction)
    }

    interface Box : MoveableObject

    interface CharItem {
        val symbol: Char
    }

    class Floor {
        companion object : CharItem {
            override val symbol = '.'
        }
    }

    data class Wall(override val x: Int, override val y: Int) : Point {
        override fun plus(direction: Direction) = Wall(x + direction.dx, y + direction.dy)

        override fun equals(other: Any?) = other is Point && x == other.x && y == other.y

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        companion object : CharItem {
            override val symbol = '#'
        }
    }

    data class RegularBox(override var x: Int, override var y: Int) : Box {
        override fun plus(direction: Direction) = RegularBox(x + direction.dx, y + direction.dy)

        override fun move(direction: Direction) {
            x += direction.dx
            y += direction.dy
        }

        @Suppress("kotlin:S2097")
        override fun equals(other: Any?) = other is Point && x == other.x && y == other.y

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        companion object : CharItem {
            override val symbol = 'O'
        }
    }

    data class LargeBoxLeft(override var x: Int, override var y: Int) : Box {
        override fun plus(direction: Direction) = LargeBoxLeft(x + direction.dx, y + direction.dy)

        override fun move(direction: Direction) {
            x += direction.dx
            y += direction.dy
        }

        @Suppress("kotlin:S2097")
        override fun equals(other: Any?) = other is Point && x == other.x && y == other.y

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        companion object : CharItem {
            override val symbol = '['
        }
    }

    data class LargeBoxRight(override var x: Int, override var y: Int) : Box {
        override fun plus(direction: Direction) = LargeBoxRight(x + direction.dx, y + direction.dy)

        override fun move(direction: Direction) {
            x += direction.dx
            y += direction.dy
        }

        @Suppress("kotlin:S2097")
        override fun equals(other: Any?) = other is Point && x == other.x && y == other.y

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        companion object : CharItem {
            override val symbol = ']'
        }
    }

    data class Robot(override var x: Int, override var y: Int) : MoveableObject {
        override fun plus(direction: Direction) = Robot(x + direction.dx, y + direction.dy)

        override fun move(direction: Direction) {
            x += direction.dx
            y += direction.dy
        }

        @Suppress("kotlin:S2097")
        override fun equals(other: Any?) = other is Point && x == other.x && y == other.y

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        companion object : CharItem {
            override val symbol = '@'
        }
    }

    enum class Direction(val dx: Int, val dy: Int) {
        TOP(0, -1),
        BOTTOM(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        fun isVertical() = this == TOP || this == BOTTOM

        fun opposite() = when (this) {
            TOP -> BOTTOM
            BOTTOM -> TOP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }

        companion object {
            fun fromSymbol(symbol: Char) = when (symbol) {
                '^' -> TOP
                'v' -> BOTTOM
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("Invalid direction $symbol")
            }
        }
    }

    fun checkVerticalSurroundings(
        origin: Point,
        direction: Direction,
        moveableObjects: Collection<MoveableObject>,
        toMove: MutableCollection<MoveableObject>
    ) {
        val item = moveableObjects.find { it == origin + direction } ?: return
        when (item) {
            is RegularBox -> if (toMove.add(item)) {
                checkVerticalSurroundings(item, direction, moveableObjects, toMove)
            }

            is LargeBoxLeft -> if (toMove.add(item)) {
                checkVerticalSurroundings(item, direction, moveableObjects, toMove)
                val rightPart = moveableObjects.find { it == item + Direction.RIGHT } ?: return
                if (rightPart is LargeBoxRight && toMove.add(rightPart)) {
                    checkVerticalSurroundings(rightPart, direction, moveableObjects, toMove)
                }
            }

            is LargeBoxRight -> {
                toMove.add(item)
                checkVerticalSurroundings(item, direction, moveableObjects, toMove)
                val leftPart = moveableObjects.find { it == item + Direction.LEFT } ?: return
                if (leftPart is LargeBoxLeft && toMove.add(leftPart)) {
                    checkVerticalSurroundings(leftPart, direction, moveableObjects, toMove)
                }
            }
        }
    }

    fun tryMovingItems(items: Collection<Point>, direction: Direction) {
        val robot = items.filterIsInstance<Robot>().first()

        if (items.none { it == robot + direction }) {
            // no obstacle next to the robot, we can move it freely
            robot.move(direction)
            return
        }

        // if it wants to go in a wall, don't
        if (items.find { it == robot + direction } is Wall) {
            return
        }

        val moveableObjects = items.filterIsInstance<MoveableObject>()
        val walls = items.filterIsInstance<Wall>()
        val impactedBoxes: List<MoveableObject> = if (direction.isVertical()) {
            val impacted = mutableSetOf<MoveableObject>()
            checkVerticalSurroundings(robot, direction, moveableObjects, impacted)
            impacted.toList()
        } else emptyList()
        val impactedColumnsToStart = impactedBoxes.groupBy { box -> box.x }.mapValues { (_, boxes) ->
            when (direction) {
                Direction.TOP -> boxes.maxBy { it.y }.y
                Direction.BOTTOM -> boxes.minBy { it.y }.y
                else -> 0 // cannot happen
            }
        }

        // otherwise, figure out if there is at least one free space
        // between the robot and the closest wall in that direction…
        val posToMove = mutableSetOf<MoveableObject>(robot)
        if (direction.isVertical()) {
            // "simply" need to check whether the farthest point from the closest
            // wall can move; if it can, all the others can
            if (impactedColumnsToStart.any { (column, columnStart) ->
                    val closestWall = walls.let {
                        var pos: Point = RegularBox(column, columnStart)
                        while (pos !in it) { // this won't loop forever because there is ALWAYS a wall at some point (borders)
                            pos += direction
                        }
                        pos
                    }
                    if (closestWall + direction.opposite() in impactedBoxes) return // no free space
                    val yRange = min(columnStart, closestWall.y)..max(columnStart, closestWall.y)
                    moveableObjects.filter { item ->
                        item.x == column && item.y in yRange
                    }.size == yRange.last - yRange.first
                }) {
                // no free space for at least one of the columns
                return
            }
            posToMove.addAll(impactedBoxes)
        } else {
            val closestWall = walls.let {
                var pos: Point = robot
                while (pos !in it) {
                    pos += direction
                }
                pos
            }
            val xRange = min(robot.x, closestWall.x)..max(robot.x, closestWall.x)
            val itemsBetweenWandR = moveableObjects
                .filter { item -> item.x in xRange && item.y == robot.y }

            if (itemsBetweenWandR.size == xRange.last - xRange.first) {
                // no free space
                return
            }

            for (x in xRange.let { if (direction == Direction.LEFT) it.reversed() else it }) {
                itemsBetweenWandR.find { it.x == x }?.let {
                    posToMove.add(it)
                } ?: break
            }
        }

        // …if so, move all the items that need to be moved
        for (pos in posToMove) {
            pos.move(direction)
        }
    }

    fun parseMap(input: List<String>): Collection<Point> {
        val points = mutableSetOf<Point>()
        for ((j, line) in input.withIndex()) {
            if (line.isEmpty()) break
            for ((i, char) in line.withIndex()) {
                when (char) {
                    Wall.symbol -> points.add(Wall(i, j))
                    RegularBox.symbol -> points.add(RegularBox(i, j))
                    LargeBoxLeft.symbol -> points.add(LargeBoxLeft(i, j))
                    LargeBoxRight.symbol -> points.add(LargeBoxRight(i, j))
                    Robot.symbol -> points.add(Robot(i, j))
                    Floor.symbol -> {
                        // Ignore this
                    }

                    else -> error("Invalid character $char")
                }
            }
        }
        return points.toList()
    }

    override fun solve(input: List<String>) {
        // Parsing
        val map = parseMap(input)
        var robotMoves = emptyList<Direction>()
        for ((j, line) in input.withIndex()) {
            if (line.isEmpty()) {
                robotMoves = input.subList(j + 1, input.size).joinToString("").map(Direction::fromSymbol)
                break
            }
        }

        // Part 1
        for (move in robotMoves) {
            tryMovingItems(map, move)
        }

        setPart1Answer(map.filter { it is Box }.sumOf { it.x + it.y * 100 })

        // Part 2
        val upscaledInput = input.map { line ->
            line.map { char ->
                when (char) {
                    Wall.symbol, Floor.symbol -> "$char$char"
                    RegularBox.symbol -> "${LargeBoxLeft.symbol}${LargeBoxRight.symbol}"
                    Robot.symbol -> "$char${Floor.symbol}"
                    else -> char
                }
            }.joinToString("")
        }
        val upscaledMap = parseMap(upscaledInput)

        for (move in robotMoves) {
            tryMovingItems(upscaledMap, move)
        }

        setPart2Answer(upscaledMap.filter { it is LargeBoxLeft }.sumOf { it.x + it.y * 100 })
    }
}
