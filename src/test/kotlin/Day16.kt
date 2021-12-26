import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class Day16 {
    val regex = "Sue (.*?): (.*)".toRegex()

    @Test
    fun part1() {
        val dataset = loadDataset()
        val input = mapOf(
            "children" to 3,
            "cats" to 7,
            "samoyeds" to 2,
            "pomeranians" to 3,
            "akitas" to 0,
            "vizslas" to 0,
            "goldfish" to 5,
            "trees" to 3,
            "cars" to 2,
            "perfumes" to 1
        )

        val aunt = dataset
            .filter { it.second.all { (k,v) -> input[k]!! == v } }
            .single()

        assertEquals(373, aunt.first)
    }

    @Test
    fun part2() {
        val dataset = loadDataset()
        val input = mapOf(
            "children" to 3,
            "cats" to 7,
            "samoyeds" to 2,
            "pomeranians" to 3,
            "akitas" to 0,
            "vizslas" to 0,
            "goldfish" to 5,
            "trees" to 3,
            "cars" to 2,
            "perfumes" to 1
        )

        val aunt = dataset
            .filter { it.second.all { (k,v) ->
                when(k) {
                    "cats" -> input[k]!! < v
                    "trees" -> input[k]!! < v
                    "pomeranians" -> input[k]!! > v
                    "goldfish" -> input[k]!! > v
                    else -> input[k]!! == v
                }
            } }
            .single()

        assertEquals(260, aunt.first)
    }

    private fun loadDataset(): List<Pair<Int, Map<String, Int>>> {
        val lines = File(ClassLoader.getSystemResource("day16.txt").file).readLines()
        val x = lines
            .map { regex.matchEntire(it)!! }
            .map { Pair(it.groupValues[1], it.groupValues[2]) }
            .map { (n, atts) ->
                val attMap = atts
                    .split(",")
                    .map {
                        val s = it.split(":")
                        Pair(s[0].trim(), s[1].trim().toInt())
                    }
                    .toMap()
                Pair(n.toInt(), attMap)
            }
        return x
    }
}