package days

import kotlin.reflect.full.companionObjectInstance
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {
    fun getStaticSymbol(item: Day15.Point) =
        (item::class.companionObjectInstance as? Day15.CharItem)?.symbol ?: Day15.Floor.symbol

    fun getMapRepresentation(points: Collection<Day15.Point>): Collection<String> {
        val maxWidth = points.map { it.y }.toSet().maxOfOrNull { line ->
            points.filter { it.y == line }.maxBy { it.x }.x
        } ?: 0
        val maxHeight = points.map { it.x }.toSet().maxOfOrNull { line ->
            points.filter { it.x == line }.maxBy { it.y }.y
        } ?: 0

        val r = (0..maxHeight).map { y ->
            (0..maxWidth).map { x ->
                points.find { it.x == x && it.y == y }?.let(::getStaticSymbol) ?: Day15.Floor.symbol
            }.joinToString("")
        }

        return r
    }

    fun mapRepresentationToString(representation: Collection<String>) =
        if (representation.size > 1) "\n" + representation.joinToString("\n") + "\n"
        else representation.joinToString("\n")

    fun assertMapMoves(
        expected: Collection<String>,
        actual: Collection<String>,
        withMoves: Collection<Day15.Direction>
    ) {
        val expectedMap = Day15.parseMap(expected.toList())
        val actualMap = Day15.parseMap(actual.toList())

        for (move in withMoves) {
            Day15.tryMovingItems(actualMap, move)
        }

        assertEquals(
            mapRepresentationToString(getMapRepresentation(expectedMap)),
            mapRepresentationToString(getMapRepresentation(actualMap))
        )
    }

    @Test
    fun testFreeMoveVertical() {
        assertMapMoves(
            listOf(
                "#",
                "@",
                "."
            ),
            listOf(
                "#",
                ".",
                "@"
            ),
            listOf(Day15.Direction.TOP)
        )
        assertMapMoves(
            listOf(
                ".",
                "@",
                "#"
            ),
            listOf(
                "@",
                ".",
                "#"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testFreeMoveHorizontal() {
        assertMapMoves(
            listOf(".@#"),
            listOf("@.#"),
            listOf(Day15.Direction.RIGHT)
        )
        assertMapMoves(
            listOf("#@."),
            listOf("#.@"),
            listOf(Day15.Direction.LEFT)
        )
    }

    @Test
    fun testVerticalWall() {
        assertMapMoves(
            listOf(
                "#",
                "@"
            ),
            listOf(
                "#",
                "@"
            ),
            listOf(Day15.Direction.TOP)
        )
        assertMapMoves(
            listOf(
                "@",
                "#"
            ),
            listOf(
                "@",
                "#"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testHorizontalWall() {
        assertMapMoves(
            listOf("@#"),
            listOf("@#"),
            listOf(Day15.Direction.RIGHT)
        )
        assertMapMoves(
            listOf("#@"),
            listOf("#@"),
            listOf(Day15.Direction.LEFT)
        )
    }

    @Test
    fun testRegularVertical() {
        assertMapMoves(
            listOf(
                "#",
                "O",
                "O",
                "@",
                "."
            ),
            listOf(
                "#",
                ".",
                "O",
                "O",
                "@"
            ),
            listOf(Day15.Direction.TOP)
        )
        assertMapMoves(
            listOf(
                ".",
                "@",
                "O",
                "O",
                "#"
            ),
            listOf(
                "@",
                "O",
                "O",
                ".",
                "#"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testRegularHorizontal() {
        assertMapMoves(
            listOf(".@OO#"),
            listOf("@OO.#"),
            listOf(Day15.Direction.RIGHT)
        )
        assertMapMoves(
            listOf("#OO@."),
            listOf("#.OO@"),
            listOf(Day15.Direction.LEFT)
        )
    }

    @Test
    fun testRegularNoUselessPushVertical() {
        assertMapMoves(
            listOf(
                "##",
                ".O",
                "OO",
                ".@"
            ),
            listOf(
                "##",
                "..",
                "OO",
                ".O",
                ".@"
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeVertical() {
        assertMapMoves(
            listOf(
                "##",
                "[]",
                "].",
                "@.",
                ".."
            ),
            listOf(
                "##",
                "..",
                "[]",
                "].",
                "@."
            ),
            listOf(Day15.Direction.TOP)
        )
        assertMapMoves(
            listOf(
                "..",
                "@.",
                "].",
                "[]",
                "##"
            ),
            listOf(
                "@.",
                "].",
                "[]",
                "..",
                "##"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testLargeHorizontal() {
        assertMapMoves(
            listOf(".@[][]#"),
            listOf("@[][].#"),
            listOf(Day15.Direction.RIGHT)
        )
        assertMapMoves(
            listOf("#[][]@."),
            listOf("#.[][]@"),
            listOf(Day15.Direction.LEFT)
        )
    }

    @Test
    fun testLargeNoUselessPushVertical() {
        assertMapMoves(
            listOf(
                "####",
                "..[]",
                "[][]",
                "..@."
            ),
            listOf(
                "####",
                "....",
                "[][]",
                "..[]",
                "..@."
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionVertical() {
        assertMapMoves(
            listOf(
                "######",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(
                "######",
                "......",
                ".[][].",
                "..[]..",
                "...@.."
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionNoUselessPushVertical() {
        assertMapMoves(
            listOf(
                "#####",
                ".[][]",
                "..[].",
                "[].@."
            ),
            listOf(
                "#####",
                ".....",
                ".[][]",
                "[][].",
                "...@."
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionNoMoveVertical() {
        assertMapMoves(
            listOf(
                "######",
                "...#..",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(
                "######",
                "...#..",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionOffsetWallVertical() {
        assertMapMoves(
            listOf(
                "######",
                ".#....",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(
                "######",
                ".#....",
                "......",
                ".[][].",
                "..[]..",
                "...@.."
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionOffsetWallNoMoveVertical() {
        assertMapMoves(
            listOf(
                "######",
                "....#.",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(
                "######",
                "....#.",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeChainReactionOffsetWallNoMoveVertical2() {
        assertMapMoves(
            listOf(
                "..@...",
                "..[]..",
                "..[]..",
                "...[].",
                "..[]..",
                "######"
            ),
            listOf(
                "..@...",
                "..[]..",
                "..[]..",
                "...[].",
                "..[]..",
                "######"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testLargeChainReactionOffsetWallNoUselessPushVertical() {
        assertMapMoves(
            listOf(
                "#####",
                "..[].",
                ".[][]",
                "..[].",
                "[].@."
            ),
            listOf(
                "#####",
                "..[].",
                ".....",
                ".[][]",
                "[][].",
                "...@."
            ),
            listOf(Day15.Direction.TOP)
        )
    }

    @Test
    fun testLargeSpaceBeforeWallVertical() {
        assertMapMoves(
            listOf(
                "######",
                "[].[].",
                ".[][].",
                "..[]..",
                "...@..",
                "......"
            ),
            listOf(
                "######",
                "[].[].",
                "......",
                ".[][].",
                "..[]..",
                "...@.."
            ),
            listOf(Day15.Direction.TOP)
        )
        assertMapMoves(
            listOf(
                "......",
                "...@..",
                "..[]..",
                ".[][].",
                "[].[].",
                "######"
            ),
            listOf(
                "...@..",
                "..[]..",
                ".[][].",
                "......",
                "[].[].",
                "######"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testLargeSpaceBeforeWallHorizontal() {
        assertMapMoves(
            listOf(".@[][]#"),
            listOf("@[].[]#"),
            listOf(Day15.Direction.RIGHT)
        )
        assertMapMoves(
            listOf("#[][]@."),
            listOf("#[].[]@"),
            listOf(Day15.Direction.LEFT)
        )
    }

    // After resolving the most common patterns, these
    // are the issues/last edge cases that prevented me
    // from getting the right answer for part 2
    // (they were obviously all related to the large vertical moves,
    // the hardest part of all this)

    @Test
    fun testIssue1() {
        assertMapMoves(
            listOf(
                "....",
                ".@[]",
                "[]..",
                ".[].",
                "##[]", // the box here caused the boxes above to refuse to move
                "####"
            ),
            listOf(
                ".@..",
                "[][]",
                ".[].",
                "....",
                "##[]",
                "####"
            ),
            listOf(Day15.Direction.BOTTOM)
        )
    }

    @Test
    fun testIssue2() {
        assertMapMoves(
            listOf(
                "####",
                "...#",
                "[][]", // this rightmost box side was ignored, and the whole thing was pushed anyway
                ".[].",
                "..[]",
                "...@"
            ),
            listOf(
                "####",
                "...#",
                "[][]",
                ".[].",
                "..[]",
                "...@"
            ),
            listOf(Day15.Direction.TOP)
        )
    }
}
