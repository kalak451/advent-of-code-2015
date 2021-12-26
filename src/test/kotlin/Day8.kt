import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day8 {
    @Test
    fun shouldCountMemory() {
        assertEquals(0, memorySize("\"\""))
        assertEquals(3, memorySize("\"abc\""))
        assertEquals(7, memorySize("\"aaa\\\"aaa\""))
        assertEquals(1, memorySize("\"\\x27\""))
    }

    @Test
    fun shouldCountEncoded() {
        assertEquals(6, encodedSize("\"\""))
        assertEquals(9, encodedSize("\"abc\""))
        assertEquals(16, encodedSize("\"aaa\\\"aaa\""))
        assertEquals(11, encodedSize("\"\\x27\""))
    }

    @Test
    fun shouldDifSizes() {
        val overallSizeDif = listOf(
            "\"\"",
            "\"abc\"",
            "\"aaa\\\"aaa\"",
            "\"\\x27\""
        ).sumOf { sizeDif(it) }

        assertEquals(12, overallSizeDif)
    }

    @Test
    fun shouldDifEncodedSizes() {
        val overallSizeDif = listOf(
            "\"\"",
            "\"abc\"",
            "\"aaa\\\"aaa\"",
            "\"\\x27\""
        ).sumOf { encodedSizeDif(it) }

        assertEquals(19, overallSizeDif)
    }


    @Test
    fun part1() {
        val lines = File(ClassLoader.getSystemResource("day8.txt").file).readLines()

        assertEquals(1350, lines.sumOf { sizeDif(it) })
    }

    @Test
    fun part2() {
        val lines = File(ClassLoader.getSystemResource("day8.txt").file).readLines()

        assertEquals(2085, lines.sumOf { encodedSizeDif(it) })
    }

    private fun sizeDif(s: String): Int {
        return codeSize(s) - memorySize(s)
    }

    private fun encodedSizeDif(s: String): Int {
        return encodedSize(s) - codeSize(s)
    }

    private fun codeSize(s: String): Int {
        return s.length
    }

    private fun memorySize(s: String): Int {
        val regex = "(\\\\\\\\|\\\\\"|\\\\x(\\d|[a-f])(\\d|[a-f]))".toRegex()

        val replace = regex.replace(s, "_")
        val i = replace.length - 2
        return i
    }

    private fun encodedSize(s: String): Int {
        val slashRegex = "\\\\".toRegex()
        val quoteRegex = "\"".toRegex()

        val slashesReplaced = slashRegex.replace(s, "__")
        val quotesReplaced = quoteRegex.replace(slashesReplaced, "__")
        val result = "_" + quotesReplaced + "_"
        return result.length
    }
}