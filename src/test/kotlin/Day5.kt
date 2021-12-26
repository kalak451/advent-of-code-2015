import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.min
import kotlin.test.assertEquals

class Day5 {
    @Test
    fun part1() {
        Assertions.assertEquals(255, runPart1("day5.txt"))
    }

    private fun runPart1(path: String): Long {
        return File(ClassLoader.getSystemResource(path).file).readLines()
            .filter { it.filter { c -> setOf('a', 'e', 'i', 'o', 'u').contains(c) }.count() >= 3 }
            .filter { it -> it.windowed(2, 1).any { it[0] == it[1] } }
            .filter { it.windowed(2, 1).toSet().intersect(setOf("ab", "cd", "pq", "xy")).isEmpty() }
            .count()
            .toLong()
    }

    @Test
    fun part2() {
        assertEquals(true, process2Part1("xyxy"))
        assertEquals(true, process2Part1("aabcdefgaa"))
        assertEquals(false, process2Part1("aaa"))
        assertEquals(true, process2Part1("abcdeaafgaaa"))
        assertEquals(true, process2Part1("xilodxfuxphuiiii"))
        assertEquals(true, process2Part1("pzkkkkwrlvxiuysn"))
        assertEquals(true, process2Part1("bkkkkcwegvypbrio"))

        assertEquals(true, process2Part2("xyx"))
        assertEquals(true, process2Part2("abcdefeghi"))
        assertEquals(true, process2Part2("aaa"))
        assertEquals(true, process2Part2("xilodxfuxphuiiii"))
        assertEquals(true, process2Part2("pzkkkkwrlvxiuysn"))
        assertEquals(true, process2Part2("bkkkkcwegvypbrio"))

        assertEquals(1, process2(listOf("qjhvhtzxzqqjkmpb")))
        assertEquals(1, process2(listOf("xxyxx")))
        assertEquals(0, process2(listOf("uurcxstgmygtbstg")))
        assertEquals(0, process2(listOf("ieodomkazucvgmuy")))
        assertEquals(55, runPart2("day5.txt"))
    }

    @Test
    fun compare() {
        val lines = File(ClassLoader.getSystemResource("day5.txt").file).readLines()

        val normal = lines
            .filter { process2Part1(it) }
            .filter { process2Part2(it) }
            .toSet()

        val regex = lines
            .filter { process2Part1Regex(it) }
            .filter { process2Part2Regex(it) }
            .toSet()

        val minus = regex.minus(normal)

        assertEquals(3, minus.size)
    }

    private fun runPart2(path: String): Long {
        val lines = File(ClassLoader.getSystemResource(path).file).readLines()
        return process2(lines)
    }

    private fun process2(lines: List<String>): Long {
        return lines
            .filter { process2Part1(it) }
            .filter { process2Part2(it) }
            .count()
            .toLong()
    }



    private fun process2Part1(s: String): Boolean {
        return s.windowed(2, 1)
            .withIndex()
            .groupBy { it.value }
            .values
            .filter { it.size > 1 }
            .any { it.size > 2 || abs(it[0].index - it[1].index) > 1 }
    }

    private fun process2Part1Regex(s: String): Boolean {
        return "(..).*\\1".toRegex().containsMatchIn(s)
    }

    private fun process2Part2(it: String): Boolean {
        return it.windowed(3, 1).any { it[0] == it[2] }
    }

    private fun process2Part2Regex(it: String): Boolean {
        return "(.).\\1".toRegex().containsMatchIn(it)
    }
}