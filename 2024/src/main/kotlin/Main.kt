import days.DayBase
import java.io.OutputStream
import java.io.PrintStream
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

enum class Colors {
    RESET, BOLD, RED, GREEN,
    BLUE,
    CYAN;

    override fun toString() = when (this) {
        RESET -> "\u001b[0m"
        BOLD -> "\u001b[1m"
        RED -> "\u001b[31m"
        GREEN -> "\u001b[32m"
        BLUE -> "\u001b[34m"
        CYAN -> "\u001b[36m"
    }
}

/**
 * Returns a hardcoded "zero value" of the given
 * type in a type-safe manner.
 * Supports all primitive types.
 */
fun <T : Any> getZeroValue(type: KClass<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return when (type) {
        Int::class -> 0 as T
        Double::class -> 0.0 as T
        Float::class -> 0f as T
        Boolean::class -> false as T
        String::class -> "" as T
        List::class -> emptyList<Any>() as T
        Set::class -> emptySet<Any>() as T
        Map::class -> emptyMap<Any, Any>() as T
        else -> try {
            type.createInstance()
        } catch (e: Exception) {
            println("Could not instantiate ${type.java.name}: ${e.message}")
            null
        }
    }
}

/**
 * Resets a class-object's properties (mutable `val` or `var`)
 * back to their default value.
 *
 * For "mutable `val`s" (e.g., `val thing = mutableListOf<Int>()`),
 * clear it (only supports collections and maps).
 *
 * For `var`s (e.g., `var thing: Int = 0`), reset them to their default
 * value by setting them to the return of [getZeroValue].
 *
 * Non-mutable `val` are intentionally left untouched, both because it's
 * technically not possible to alter them and because we don't want to
 * reset constants, which are **very likely** identical between different
 * executions.
 */
fun resetObjectProperties(obj: Any) {
    for (property in obj::class.declaredMemberProperties) {
        property.isAccessible = true
        if (property is KMutableProperty<*>) {
            val zeroValue = getZeroValue(property.returnType.classifier as? KClass<*> ?: continue)
            try {
                property.setter.call(obj, zeroValue)
            } catch (_: Exception) {
                println("${Colors.RED}Could not reset field \"${property.name}\", skipping${Colors.RESET}")
            }
            continue
        }
        val value = property.call(obj)
        when (value) {
            is MutableCollection<*> -> value.clear()
            is MutableMap<*, *> -> value.clear()
            else -> error("Unexpected property type ${value?.javaClass}")
        }
    }
}

/**
 * Pretty-prints the [answer] for [day] [part].
 * Adds an emoji depending on the value of [matchesExample] (null, true or false).
 */
fun printAnswer(day: Int, part: Int, answer: Long, matchesExample: Boolean? = null) {
    val formattedOutput = "%,d".format(answer)
    val output = if (formattedOutput != "$answer") "\t($formattedOutput)" else ""
    val tick = when (matchesExample) {
        true -> " ✅"
        false -> " ❌"
        null -> ""
    }
    println("${Colors.BLUE}[Day $day]${Colors.CYAN} Part $part:${Colors.RESET} $answer$output$tick")
}

/**
 * Pretty-prints the execution time passed as a parameter with an
 * additional empty line before.
 */
fun printExecutionTime(executionTime: Duration) {
    println(
        "\n${Colors.BOLD}Execution time: ${
            if (executionTime > 1.seconds) {
                Colors.RED
            } else {
                Colors.GREEN
            }
        }$executionTime${Colors.RESET}"
    )
}

fun main() {
    val d = DayBase.getAllDays().maxByOrNull { it.day } ?: run {
        println("No implementation found. Implement the DayBase class to begin!")
        return
    }

    // Compute example
    val example = Examples.get(d.day)
    if (!d.testMode) {
        val out = System.out
        System.setOut(PrintStream(OutputStream.nullOutputStream()))
        example?.input?.let { d.solve(it) }
        System.setOut(out)
    }
    val d1example = d._part1Answer?.also { d._part1Answer = null }
    val d2example = d._part2Answer?.also { d._part2Answer = null }

    // Reset day before real compute
    resetObjectProperties(d)

    // Compute the real input
    val input = if (d.testMode) {
        example?.input ?: throw Exception("No example input despite test mode enabled")
    } else d.readInput()
    val execTime = measureTime { d.solve(input) }

    if (d._part1Answer != null || d._part2Answer != null) println()
    d._part1Answer?.let { ans ->
        printAnswer(
            d.day,
            1,
            ans,
            example?.part1Answer?.let { p1 ->
                (if (d.testMode) ans else d1example)?.let { it == p1 }
            }
        )
    }
    d._part2Answer?.let { ans ->
        printAnswer(
            d.day,
            2,
            ans,
            example?.part2Answer?.let { p2 ->
                (if (d.testMode) ans else d2example)?.let { it == p2 }
            }
        )
    }
    printExecutionTime(execTime)
}
