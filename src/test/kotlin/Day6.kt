import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.lang.RuntimeException


class Day6 {
    @Test
    fun part1() {
        val lines = File(ClassLoader.getSystemResource("day6.txt").file).readLines()
        val grid = (0 until 1000).map { (0 until 1000).map { false }.toMutableList() }.toMutableList()

        lines
            .map { parse(it) }
            .forEach { applyRule(it, grid) }

        assertEquals(569999, grid.flatten().count { it })
    }

    @Test
    fun part2() {
        val lines = File(ClassLoader.getSystemResource("day6.txt").file).readLines()
        val grid = (0 until 1000).map { (0 until 1000).map { 0 }.toMutableList() }.toMutableList()

        lines
            .map { parse(it) }
            .forEach { applyLevelRule(it, grid) }

        assertEquals(17836115, grid.flatten().sumOf { it })
    }

    private fun applyRule(it: Triple<String, Pair<Int, Int>, Pair<Int, Int>>, grid: MutableList<MutableList<Boolean>>) {
        (it.second.first..it.third.first).forEach { x ->
            (it.second.second..it.third.second).forEach { y ->
                when (it.first) {
                    "turn on" -> grid[y][x] = true
                    "turn off" -> grid[y][x] = false
                    "toggle" -> grid[y][x] = !grid[y][x]
                    else -> throw RuntimeException("Invalid command ${it.first}")
                }
            }
        }
    }

    private fun applyLevelRule(it: Triple<String, Pair<Int, Int>, Pair<Int, Int>>, grid: MutableList<MutableList<Int>>) {
        (it.second.first..it.third.first).forEach { x ->
            (it.second.second..it.third.second).forEach { y ->
                when (it.first) {
                    "turn on" -> grid[y][x] += 1
                    "turn off" -> grid[y][x] = if(grid[y][x] == 0) 0 else grid[y][x] - 1
                    "toggle" -> grid[y][x] += 2
                    else -> throw RuntimeException("Invalid command ${it.first}")
                }
            }
        }
    }

    private fun parse(s: String): Triple<String, Pair<Int, Int>, Pair<Int, Int>> {
        val mr = "(.*) (.*),(.*) through (.*),(.*)".toRegex().matchEntire(s)!!

        return Triple(
            mr.groupValues[1],
            Pair(mr.groupValues[2].toInt(), mr.groupValues[3].toInt()),
            Pair(mr.groupValues[4].toInt(), mr.groupValues[5].toInt())
        )
    }

}