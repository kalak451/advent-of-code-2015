import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val TARGET = 36000000

class Day20 {
    @Test
    fun part1() {
        val input = TARGET / 10
        val houses = Array<Int?>(input) { null }
        var houseNumber = input

        for (i in 1 until input) {
            for (j in i until input step i) {
                houses[j] = (houses[j] ?: 10) + (i * 10)

                if (houses[j]!! >= TARGET && j < houseNumber) {
                    houseNumber = j
                }
            }
        }

        assertEquals(831600, houseNumber)
    }

    @Test
    fun part2() {
        val input = TARGET / 10
        val houses = Array<Int?>(input) { null }
        var houseNumber = input

        for (i in 1 until input) {
            var visits = 0
            for (j in i until input step i) {
                houses[j] = (houses[j] ?: 11) + (i * 11)

                if (houses[j]!! >= TARGET && j < houseNumber) {
                    houseNumber = j
                }

                visits++
                if (visits == 50) break
            }

        }

        assertEquals(884520, houseNumber)
    }
}