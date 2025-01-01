package days


@Suppress("unused")
object Day13 : DayBase(13) {
    data class Position(val x: Long, val y: Long)
    data class Button(val letter: Char, val price: Int, val xIncr: Int, val yIncr: Int)
    data class Machine(val prize: Position, val a: Button, val b: Button)

    const val MAX_BUTTON_PRESSES = 100
    const val PT2_INCREASE_FACTOR = 10_000_000_000_000
    var machines = listOf<Machine>()

    fun parseMachines(input: Collection<String>): List<Machine> {
        var prizePosition = Position(0, 0)
        var a = Button('0', 0, 0, 0)
        var b = Button('0', 0, 0, 0)

        val letterRegex = Regex("[a-z]")
        val prizeRegex = Regex("[a-z]=")

        val m = mutableListOf<Machine>()

        for (line in input) {
            if (line.isEmpty()) {
                m.add(Machine(prizePosition, a, b))
                continue
            }

            with(line.lowercase()) {
                when {
                    startsWith("button ") -> {
                        val (x, y) = split(": ")[1].split(", ").map { it.replace(letterRegex, "") }
                        val buttonLetter = split(": ")[0].replace("button ", "").toList().first()
                        when (buttonLetter) {
                            'a' -> a = Button(buttonLetter.uppercaseChar(), 3, x.toInt(), y.toInt())
                            'b' -> b = Button(buttonLetter.uppercaseChar(), 1, x.toInt(), y.toInt())
                        }
                    }

                    startsWith("prize:") -> {
                        val (x, y) = split(": ")[1].split(", ").map { it.replace(prizeRegex, "") }
                        prizePosition = Position(x.toLong(), y.toLong())
                    }
                }
            }
        }
        m.add(Machine(prizePosition, a, b))

        return m
    }

    fun solveMachine(machine: Machine, maxPresses: Int? = null): Long? {
        // Done with the mathematical help of ChatGPT & Claude, because I'm a math fool.
        // I just know that it's a system of equations:
        // n * Ax + m * Bx = Px
        // n * Ay + m * By = Py
        // where we have to find m and n.
        val det = machine.a.xIncr * machine.b.yIncr - machine.a.yIncr * machine.b.xIncr
        if (det == 0) return null // impossible system or infinite solution; we want only one

        val numA = machine.prize.x * machine.b.yIncr - machine.prize.y * machine.b.xIncr
        if (numA % det != 0L) return null // no non-float solution

        val aPresses = numA / det
        maxPresses?.let { max ->
            if (aPresses > max) return null // greater than max presses
        }

        val numB = machine.prize.x - aPresses * machine.a.xIncr
        if (numB % machine.b.xIncr != 0L) return null // no non-float solution

        val bPresses = numB / machine.b.xIncr
        maxPresses?.let { max ->
            if (bPresses > max) return null // greater than max presses
        }

        val eq1 = aPresses * machine.a.xIncr + bPresses * machine.b.xIncr
        val eq2 = aPresses * machine.a.yIncr + bPresses * machine.b.yIncr
        if (eq1 != machine.prize.x || eq2 != machine.prize.y) return null // invalid answer for the system

        // multiply button presses by their price and sum the result
        return aPresses * machine.a.price + bPresses * machine.b.price
    }

    override fun solve(input: List<String>) {
        // Parsing
        machines = parseMachines(input)

        // Part 1
        setPart1Answer(machines.mapNotNull { solveMachine(it, MAX_BUTTON_PRESSES) }.sum())

        // Part 2
        setPart2Answer(machines.mapNotNull { machine ->
            solveMachine(
                Machine(
                    Position(machine.prize.x + PT2_INCREASE_FACTOR, machine.prize.y + PT2_INCREASE_FACTOR),
                    machine.a,
                    machine.b
                )
            )
        }.sum())
    }
}
