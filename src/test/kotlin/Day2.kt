import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.util.regex.Pattern

class Day2 {
    @Test
    fun part1() {
        Assertions.assertEquals(1606483, runPart1("day2.txt"))
    }

    private fun runPart1(path: String): Long {
        val pattern = Pattern.compile("(.*)x(.*)x(.*)")
        return File(ClassLoader.getSystemResource(path).file).readLines()
            .asSequence()
            .map { pattern.matcher(it) }
            .filter { it.matches() }
            .map { Triple(it.group(1).toInt(), it.group(2).toInt(), it.group(3).toInt()) }
            .map { (l, w, h) -> listOf((l * w), (l * h), (h * w)).flatMap { listOf(it, it) } }
            .map { l -> l.sum() + l.minOf { it } }
            .sum()
            .toLong()
    }

    @Test
    fun part2() {
        Assertions.assertEquals(3842356, runPart2("day2.txt"))
    }

    private fun runPart2(path: String): Long {
        val pattern = Pattern.compile("(.*)x(.*)x(.*)")
        return File(ClassLoader.getSystemResource(path).file).readLines()
            .asSequence()
            .map { pattern.matcher(it) }
            .filter { it.matches() }
            .map { Triple(it.group(1).toLong(), it.group(2).toLong(), it.group(3).toLong()) }
            .map { (l, w, h) -> calculatePerimeter(l, w, h) + calculateVolume(l, w, h) }
            .sum()
    }

    private fun calculateVolume(l: Long, w: Long, h: Long): Long {
        return l * w * h
    }

    private fun calculatePerimeter(l: Long, w: Long, h: Long): Long {
        return listOf(l, w, h).sorted().take(2).sumOf { it * 2 }
    }
}