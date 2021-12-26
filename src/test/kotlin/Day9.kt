import org.junit.jupiter.api.Test
import java.io.File
import java.util.Collections.swap
import kotlin.test.assertEquals

class Day9 {

    @Test
    fun shouldBasicMin() {
        val dists = loadData("day9-test.txt")

        val min = minDistance(dists)

        assertEquals(605, min)
    }

    @Test
    fun part1() {
        val dists = loadData("day9.txt")

        val min = minDistance(dists)

        assertEquals(207, min)
    }

    @Test
    fun shouldBasicMax() {
        val dists = loadData("day9-test.txt")

        val max = maxDistance(dists)

        assertEquals(982, max)
    }

    @Test
    fun part2() {
        val dists = loadData("day9.txt")

        val max = maxDistance(dists)

        assertEquals(804, max)
    }

    private fun minDistance(dists: Map<Pair<String, String>, Int>): Int {
        val destinations = dists.keys.map { it.first }.distinct()

        val permutations = destinations.permutations()
        val min = permutations
            .map { ds -> ds.windowed(2, 1).sumOf { dd -> dists[Pair(dd[0], dd[1])]!! } }
            .minOf { it }
        return min
    }

    private fun maxDistance(dists: Map<Pair<String, String>, Int>): Int {
        val destinations = dists.keys.map { it.first }.distinct()

        val permutations = destinations.permutations()
        val min = permutations
            .map { ds -> ds.windowed(2, 1).sumOf { dd -> dists[Pair(dd[0], dd[1])]!! } }
            .maxOf { it }
        return min
    }



    fun loadData(path: String): Map<Pair<String, String>, Int> {
        val regex = "(.*) to (.*) = (.*)".toRegex()
        val lines = File(ClassLoader.getSystemResource(path).file).readLines()

        return lines
            .mapNotNull { regex.matchEntire(it) }
            .flatMap { mr ->
                val a = mr.groupValues[1]
                val b = mr.groupValues[2]
                val dist = mr.groupValues[3].toInt()

                listOf(
                    Pair(Pair(a, b), dist),
                    Pair(Pair(b, a), dist)
                )
            }
            .toMap()
    }

    fun <T> List<T>.permutations(): List<List<T>> {
        val retVal: MutableList<List<T>> = mutableListOf()

        fun generate(k: Int, list: List<T>) {
            if (k == 1) {
                retVal.add(list.toList())
            } else {
                for (i in 0 until k) {
                    generate(k - 1, list)
                    if (k % 2 == 0) {
                        swap(list, i, k - 1)
                    } else {
                        swap(list, 0, k - 1)
                    }
                }
            }
        }

        generate(this.count(), this.toList())
        return retVal
    }
}
