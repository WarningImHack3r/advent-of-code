package days

import java.nio.file.NoSuchFileException
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.exitProcess

sealed class DayBase(val day: Int) {
    companion object {
        const val CURRENT_YEAR = 2024
        fun getAllDays() = DayBase::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
    }

    var _part1Answer: Long? = null
    var _part2Answer: Long? = null

    fun readInput() = try {
        Path("$CURRENT_YEAR/inputs/input$day.txt").readLines()
    } catch (_: NoSuchFileException) {
        println("No input file found for day $day, don't forget to generate it from the script!")
        exitProcess(1)
    } catch (e: Exception) {
        println("Something went wrong while reading day $day's input: ${e.message}")
        exitProcess(1)
    }

    abstract fun solve(input: List<String>)

    fun setPart1Answer(answer: Int) {
        _part1Answer = answer.toLong()
    }

    fun setPart1Answer(answer: Long) {
        _part1Answer = answer
    }

    fun setPart2Answer(result: Int) {
        _part2Answer = result.toLong()
    }

    fun setPart2Answer(result: Long) {
        _part2Answer = result
    }
}
