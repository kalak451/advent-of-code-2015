import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class Day19 {
    @Test
    fun part1() {
        val (formulas, molecule) = loadData("day19.txt")

        val cnt = applyReplacements(formulas, molecule)
            .count()

        assertEquals(535, cnt)
    }

    private fun applyReplacements(
        formulas: List<Pair<String, String>>,
        input: String,
        invert: Boolean = false
    ): List<String> {
        return formulas.flatMap { (k, v) ->
            val vToK = v.toRegex()
                .findAll(input)
                .map { mr ->
                    input.replaceRange(mr.groups[0]!!.range, k)
                }
            val kToV = k.toRegex()
                .findAll(input)
                .map { mr ->
                    input.replaceRange(mr.groups[0]!!.range, v)
                }
            if (invert) vToK else kToV
        }
            .distinct()
    }

    @Test
    fun part2() {
        val (formulas, molecule) = loadData("day19.txt")

        val dist = mutableMapOf(molecule to 0)
        val closed = mutableSetOf<String>()
        val queue = PriorityQueue<Pair<String, Int>>(compareBy { it.second })
        queue.add(Pair(molecule, 0))

        while (queue.isNotEmpty()) {
            val (m, _) = queue.poll()

            closed.add(m)

            if (m == "e") {
                break
            }

            val filter = applyReplacements(formulas, m, invert = true)
                .filter { !closed.contains(it) }
            filter
                .forEach { nm ->
                    val newDist = dist[m]!! + 1
                    if (newDist < (dist[nm] ?: Int.MAX_VALUE)) {
                        dist[nm] = newDist
                        queue.add(Pair(nm, newDist + nm.length))
                    }
                }
        }

        assertEquals(212, dist["e"])
    }

    val formulaRegex = "(.*) => (.*)".toRegex()

    fun loadData(path: String): Pair<List<Pair<String, String>>, String> {
        val lines = File(ClassLoader.getSystemResource(path).file).readLines()

        val formulas = lines
            .takeWhile { it.isNotBlank() }
            .mapNotNull { formulaRegex.matchEntire(it) }
            .map { Pair(it.groupValues[1], it.groupValues[2]) }

        val molecule = lines
            .dropWhile { it.isNotEmpty() }
            .drop(1)
            .first()

        return Pair(formulas, molecule)
    }
}