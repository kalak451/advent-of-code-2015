import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.RuntimeException
import java.util.regex.Pattern

class Day3 {
    @Test
    fun part1() {
        Assertions.assertEquals(2081, runPart1("day3.txt"))
    }

    private fun runPart1(path: String): Long {
        val str = File(ClassLoader.getSystemResource(path).file).readLines().joinToString("")
        return str
            .runningFold(Pair(0, 0)) { (x, y), c ->
                when (c) {
                    '^' -> Pair(x, y + 1)
                    'v' -> Pair(x, y - 1)
                    '>' -> Pair(x + 1, y)
                    '<' -> Pair(x - 1, y)
                    else -> throw RuntimeException("invalid char $c")
                }
            }
            .distinct()
            .count()
            .toLong()
    }

    @Test
    fun part2() {
        Assertions.assertEquals(2341, runPart2("day3.txt"))
    }

    private fun runPart2(path: String): Long {
        val str = File(ClassLoader.getSystemResource(path).file).readLines().joinToString("")
        return str
            .withIndex()
            .runningFold(Pair(Pair(0, 0), Pair(0, 0))) { (s1, s2), ci ->
                val (idx, c) = ci
                when (idx % 2) {
                    0 -> Pair(s1, move(s2, c))
                    1 -> Pair(move(s1, c), s2)
                    else -> throw RuntimeException("err")
                }
            }
            .flatMap { it.toList() }
            .distinct()
            .count()
            .toLong()
    }

    private fun move(s: Pair<Int, Int>, c: Char): Pair<Int, Int> {
        val (x, y) = s
        return when (c) {
            '^' -> Pair(x, y + 1)
            'v' -> Pair(x, y - 1)
            '>' -> Pair(x + 1, y)
            '<' -> Pair(x - 1, y)
            else -> throw RuntimeException("invalid char $c")
        }
    }
}