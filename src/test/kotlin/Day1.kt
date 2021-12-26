import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.RuntimeException

class Day1 {
    @Test
    fun part1() {
        Assertions.assertEquals(232, runPart1("day1.txt"))
    }

    private fun runPart1(path: String): Long {
        val str = File(ClassLoader.getSystemResource(path).file).readLines().joinToString("")

        return str.fold(0) { acc, c ->
            when (c) {
                '(' -> acc + 1
                ')' -> acc - 1
                else -> throw RuntimeException("Invalid char $c")
            }
        }
    }

    @Test
    fun part2() {
        Assertions.assertEquals(0, runPart2("day1.txt"))
    }

    private fun runPart2(path: String): Long {
        val str = File(ClassLoader.getSystemResource(path).file).readLines().joinToString("")

        return str
            .runningFold(0, ) { acc, c ->
                when (c) {
                    '(' -> acc + 1
                    ')' -> acc - 1
                    else -> throw RuntimeException("Invalid char $c")
                }
            }.withIndex()
            .dropWhile { it.value >= 0 }
            .map { it.index }
            .first()
            .toLong()
    }
}
