import days.DayBase.Companion.CURRENT_YEAR
import java.nio.file.NoSuchFileException
import kotlin.io.path.Path
import kotlin.io.path.readLines

object Examples {
    private val examples = mapOf<Int, Answer>(
        1 to Answer(11, 31),
        2 to Answer(2, 4),
        3 to Answer(161, 48),
        4 to Answer(18, 9),
        5 to Answer(143, 123),
        6 to Answer(41, 6),
        7 to Answer(3_749, 11_387),
        8 to Answer(14, 34),
        9 to Answer(1_928, 2_858),
        10 to Answer(36, 81),
        11 to Answer(55_312, 65_601_038_650_482),
        12 to Answer(1_930, 1_206),
        13 to Answer(100, null),
        14 to Answer(12, null),
        15 to Answer(10_092, null),
        16 to Answer(null, null),
        17 to Answer(null, null),
        18 to Answer(null, null),
        19 to Answer(null, null),
        20 to Answer(null, null),
        21 to Answer(null, null),
        22 to Answer(null, null),
        23 to Answer(null, null),
        24 to Answer(null, null),
        25 to Answer(null, null)
    )

    private fun readInput(day: Int) = try {
        Path("$CURRENT_YEAR/examples/day$day.txt").readLines()
    } catch (_: NoSuchFileException) {
        println("No example found for day $day, don't forget to add it")
        null
    } catch (e: Exception) {
        println("Something went wrong while reading the example for day $day: ${e.message}")
        null
    }

    fun get(day: Int) = readInput(day)?.let { input ->
        val results = examples[day]
        Example(input, results?.part1Answer, results?.part2Answer)
    }

    interface Answers {
        val part1Answer: Long?
        val part2Answer: Long?
    }

    data class Answer(override val part1Answer: Long?, override val part2Answer: Long?) : Answers
    data class Example(
        val input: List<String>,
        override val part1Answer: Long?, override val part2Answer: Long?
    ) : Answers
}
