import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day17 {

    @Test
    fun part1() {
        val lines = File(ClassLoader.getSystemResource("day17.txt").file).readLines()
        val containers = lines.map { it.toInt() }
        val combos = combos(containers)

        val cnt = combos
            .map { it.sum() }
            .filter { it == 150 }
            .count()

        assertEquals(1638, cnt)
    }

    @Test
    fun part2() {
        val lines = File(ClassLoader.getSystemResource("day17.txt").file).readLines()
        val containers = lines.map { it.toInt() }
        val combos = combos(containers)

        val cnt = combos
            .map { Pair(it.sum(), it.count()) }
            .filter { it.first == 150 }
            .groupBy { it.second }
            .minByOrNull { it.key }!!
            .value
            .count()

        assertEquals(17, cnt)
    }

    @Test
    fun shouldCombo() {
        val p1 = combos(listOf())
        assertEquals(listOf(), p1)

        val p2 = combos(listOf(1))
        assertEquals(listOf(listOf(1)), p2)

        val p3 = combos(listOf(1, 2))
        assertEquals(
            listOf(
                listOf(1),
                listOf(1, 2),
                listOf(2),
            ), p3
        )

        val p4 = combos(listOf(1, 2, 3))
        assertEquals(
            listOf(
                listOf(1),
                listOf(1, 2),
                listOf(2),
                listOf(1, 2, 3),
                listOf(2, 3),
                listOf(1, 3),
                listOf(3),
            ), p4
        )
    }

    fun combos(values: List<Int>): List<List<Int>> {
        if (values.isEmpty()) {
            return listOf()
        }

        val f = values[0]
        val rest = values.slice(values.indices.drop(1))

        return listOf(listOf(f)) + combos(rest).flatMap {
            listOf(
                listOf(f) + it,
                it
            )
        }
    }
}