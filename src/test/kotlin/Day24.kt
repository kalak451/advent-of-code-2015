import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day24 {
    @Test
    fun part1() {
        val lines = File(ClassLoader.getSystemResource("day24.txt").file).readLines()
        val products = lines.map { it.toInt() }

        val total = products.sum();
        val target = total / 3

        assertEquals(508, target)


        val result = bundle(products, target)
        val ent = result.fold(1L) { acc, x -> acc * x }

        assertEquals(10439961859, ent)

    }

    @Test
    fun part2() {
        val lines = File(ClassLoader.getSystemResource("day24.txt").file).readLines()
        val products = lines.map { it.toInt() }

        val total = products.sum();
        val target = total / 4

        assertEquals(381, target)


        val result = bundle(products, target)
        val ent = result.fold(1L) { acc, x -> acc * x }

        assertEquals(0, ent)

    }

    private fun bundle(products: List<Int>, target: Int): List<Int> {
        for (i in 1..10) {
            val combos = combinations(products, i)
            val pick = combos
                .filter { c -> c.sum() == target }
                .sortedBy { c -> c.fold(1L) { acc, x -> acc * x } }
                .firstOrNull()

            if (pick != null) {
                return pick
            }
        }

        return emptyList()
    }


    fun <T> combinations(pool: List<T>, r: Int): Sequence<List<T>> {
        val n = pool.size
        if (r > n) {
            return sequenceOf()
        }

        return sequence {
            val indicies = (0 until r).toMutableList()
            while (true) {
                yield(indicies.map { pool[it] })
                var i = r - 1
                loop@ while (i >= 0) {
                    if (indicies[i] != i + n - r) break@loop
                    i--
                }
                if (i < 0) {
                    break
                }
                indicies[i] += 1
                (i + 1 until r).forEach { indicies[it] = indicies[it - 1] + 1 }
            }
        }
    }
}