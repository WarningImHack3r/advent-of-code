package days

@Suppress("unused")
object Day9 : DayBase(9) {
    sealed interface FileItem {
        var size: Int
    }

    data class EmptySpace(override var size: Int) : FileItem
    data class File(val id: Long, override var size: Int) : FileItem

    val diskItems = mutableListOf<FileItem>()
    val sortedDigits = mutableListOf<Long>()

    fun part1() {
        val newItems = diskItems.map {
            when (it) {
                is File -> it.copy()
                is EmptySpace -> it.copy()
            }
        }
        for ((i, item) in newItems.withIndex()) {
            // File
            if (item is File) {
                repeat(item.size) {
                    sortedDigits.add(item.id)
                }
                continue
            }

            // Empty space
            repeat(item.size) {
                for ((j, rItem) in newItems.asReversed().withIndex()) {
                    if (i >= newItems.size - j) return
                    if (rItem is File && rItem.size > 0) {
                        rItem.size--
                        sortedDigits.add(rItem.id)
                        break
                    }
                }
            }
        }
    }

    fun part2() {
        val newItems = diskItems.map {
            when (it) {
                is File -> it.copy()
                is EmptySpace -> it.copy()
            }
        }.toMutableList()
        for ((i, item) in newItems.withIndex()) {
            // File
            if (item is File) {
                repeat(item.size) {
                    sortedDigits.add(item.id)
                }
                continue
            }

            // Empty space
            w@ while (item.size > 0) {
                for ((j, rItem) in newItems.asReversed().withIndex()) {
                    if (i >= newItems.size - j) {
                        repeat(item.size) {
                            sortedDigits.add(-1)
                        }
                        break@w
                    }
                    if (rItem is File && rItem.size > 0 && rItem.size <= item.size) {
                        repeat(rItem.size) {
                            sortedDigits.add(rItem.id)
                        }
                        item.size -= rItem.size
                        newItems[newItems.size - 1 - j] = EmptySpace(rItem.size)
                        break
                    }
                }
            }
        }
    }

    override fun solve(input: List<String>) {
        // Parsing
        var fileCount = 0L
        for ((i, char) in input.first().withIndex()) {
            if (i % 2 == 0) {
                diskItems.add(File(fileCount++, char.digitToInt()))
                continue
            }
            diskItems.add(EmptySpace(char.digitToInt()))
        }

        // Part 1
        part1()

        setPart1Answer(sortedDigits.reduceIndexed { index, acc, i -> acc + i * index })

        // Part 2
        sortedDigits.clear()

        part2()

        setPart2Answer(sortedDigits.reduceIndexed { index, acc, i ->
            if (i < 0) acc else acc + i * index
        })
    }
}
