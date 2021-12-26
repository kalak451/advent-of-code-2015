import org.junit.jupiter.api.Test
import java.io.File
import java.lang.RuntimeException
import kotlin.test.assertEquals

class Day18 {

    @Test
    fun part1() {
        val lines = File(ClassLoader.getSystemResource("day18.txt").file).readLines()
        val lights = Lights(lines)
        val result = lights.tick(100)

        val cnt = result.countOn()

        assertEquals(814, cnt)
    }

    @Test
    fun part2() {
        val lines = File(ClassLoader.getSystemResource("day18.txt").file).readLines()
        val lights = Lights(lines, true)
        val result = lights.tick(100)

        val cnt = result.countOn()

        assertEquals(924, cnt)
    }

    data class Lights(val grid: List<String>, val stuckCorners: Boolean = false) {
        fun countOn(): Int {
            return grid.flatMapIndexed { y, row -> row.indices.map { x -> at(Pair(x, y)) } }.count { it == '#' }
        }

        fun tick(ticks: Int): Lights {
            return generateSequence(this) { it.tick() }
                .take(ticks + 1)
                .last()
        }

        fun tick(): Lights {
            return Lights(grid.mapIndexed { y, row ->
                row.mapIndexed { x, _ ->
                    when (at(Pair(x, y))) {
                        '#' -> {
                            val on = adj(Pair(x, y)).map { at(it) }.count { it == '#' }
                            if (on == 2 || on == 3) '#' else '.'
                        }
                        '.' -> {
                            val on = adj(Pair(x, y)).map { at(it) }.count { it == '#' }
                            if (on == 3) '#' else '.'
                        }
                        else -> throw RuntimeException("Err")
                    }
                }.joinToString("")
            }, stuckCorners)
        }

        fun adj(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
            val (x, y) = pos
            return listOf(
                Pair(x - 1, y - 1),
                Pair(x, y - 1),
                Pair(x + 1, y - 1),
                Pair(x - 1, y),
                Pair(x + 1, y),
                Pair(x - 1, y + 1),
                Pair(x, y + 1),
                Pair(x + 1, y + 1),
            )
        }

        fun at(pos: Pair<Int, Int>): Char {
            val (x, y) = pos

            if (x < 0 || x >= 100) {
                return '.'
            }

            if (y < 0 || y >= 100) {
                return '.'
            }

            if (stuckCorners) {
                if (
                    (x == 0 && y == 0)
                    || (x == 99 && y == 0)
                    || (x == 0 && y == 99)
                    || (x == 99 && y == 99)
                ) {
                    return '#'
                }
            }

            return grid[y][x]
        }
    }
}