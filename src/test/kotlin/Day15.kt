import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15 {

    @Test
    fun part1Test() {
        val meta = listOf(
            mapOf("capacity" to -1, "durability" to -2, "flavor" to 6, "texture" to 3, "calories" to 8),
            mapOf("capacity" to 2, "durability" to 3, "flavor" to -2, "texture" to -1, "calories" to 3),
        )

        val combos = (0..100).flatMap { a ->
            (a..100).map { b -> listOf(a, b) }
        }.filter { it.sum() == 100 }

//        assertEquals(0, combos.size)

//        val tmpCombo = listOf(listOf(44, 56))

        val answer = crunchAnswer(combos, meta)

        assertEquals(62842880, answer)
    }

    @Test
    fun part1() {
        val meta = listOf(
            mapOf("capacity" to 3, "durability" to 0, "flavor" to 0, "texture" to -3, "calories" to 2),
            mapOf("capacity" to -3, "durability" to 3, "flavor" to 0, "texture" to 0, "calories" to 9),
            mapOf("capacity" to -1, "durability" to 0, "flavor" to 4, "texture" to 0, "calories" to 1),
            mapOf("capacity" to 0, "durability" to 0, "flavor" to -2, "texture" to 2, "calories" to 8),
        )

        val combos = (0..100).asSequence().flatMap { a ->
            (0..100).asSequence().flatMap { b ->
                (0..100).asSequence().flatMap { c ->
                    (0..100).asSequence().map { d -> listOf(a, b, c, d) }
                }
            }
        }.asSequence()
            .filter { it.sum() == 100 }
            .toList()

//        assertEquals(0, combos.size)

        val answer = crunchAnswer(combos, meta)

        assertEquals(0, answer)
    }

    private fun crunchAnswer(
        combos: List<List<Int>>,
        meta: List<Map<String, Int>>
    ): Int {
        return combos.map { c ->
            c
                .mapIndexed { ing, amt ->
                    meta[ing].mapValues { (_, v) -> v * amt }
                }
                .fold(
                    mapOf(
                        "capacity" to 0,
                        "durability" to 0,
                        "flavor" to 0,
                        "texture" to 0,
                        "calories" to 0
                    )
                ) { acc, i ->
                    acc.mapValues { (k, v) -> i[k]!! + v }
                }
        }
            .filter{ it["calories"]!! == 500 }
            .map { it
                .filterKeys { k -> k != "calories" }
                .values
                .fold(1) { acc, i -> acc * if (i < 0) 0 else i }
            }
            .maxOf { it }
    }
}