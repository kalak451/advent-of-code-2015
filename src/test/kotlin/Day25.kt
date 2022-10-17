import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day25 {

    @Test
    fun part1() {
        val index = calculateIndex(2978, 3083)

        var value = 20151125L
        for (i in 2..index) {
            value = calculateCode(value)
        }

        assertEquals(2650453, value)
    }

    private fun calculateIndex(row: Int, column: Int): Long {
        var value = 1L
        for (i in 2..row) {
            value += (i - 1)
        }

        for (i in 2..column) {
            value += (row + i - 1)
        }

        return value
    }


    @Test
    fun calculateSamples() {
        assertEquals(31916031L, calculateCode(20151125))
    }

    private fun calculateCode(input: Long): Long {
        return (input * 252533L) % 33554393L
    }
}