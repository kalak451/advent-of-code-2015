import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class Day13 {

    @Test
    fun part1() {
        val data = loadData("day13.txt")
        val maxHap = calcHappiness(data)

        assertEquals(733, maxHap)
    }

    @Test
    fun part2() {
        val data = loadData("day13.txt")

        val me = data.keys
            .map { it.first }
            .distinct()
            .flatMap { listOf(Pair(Pair("Me", it), 0), Pair(Pair(it, "Me"), 0)) }

        val maxHap = calcHappiness(data + me)

        assertEquals(0, maxHap)
    }

    fun calcHappiness(data: Map<Pair<String,String>, Int>): Int {
        return data
            .keys
            .map { it.first }
            .distinct()
            .permutations()
            .map { p ->
                val names = listOf(p.last()) + p + listOf(p.first())
                names
                    .windowed(3, 1)
                    .sumOf { data[Pair(it[1], it[0])]!! + data[Pair(it[1], it[2])]!! }
            }
            .maxOf { it }
    }

    fun loadData(path: String): Map<Pair<String, String>, Int> {
        val regex = "(.*) would (.*) (.*) happiness units by sitting next to (.*)\\.".toRegex()
        val lines = File(ClassLoader.getSystemResource(path).file).readLines()

        return lines
            .mapNotNull { regex.matchEntire(it) }
            .associate {
                val dir = it.groupValues[2]
                val amount = if(dir == "gain") it.groupValues[3].toInt() else -it.groupValues[3].toInt()
                Pair(Pair(it.groupValues[1], it.groupValues[4]), amount)
            }
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
                        Collections.swap(list, i, k - 1)
                    } else {
                        Collections.swap(list, 0, k - 1)
                    }
                }
            }
        }

        generate(this.count(), this.toList())
        return retVal
    }
}