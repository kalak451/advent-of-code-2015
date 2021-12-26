import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10 {
    @Test
    fun examples() {
        assertEquals("11", lookAndSay("1"))
        assertEquals("21", lookAndSay("11"))
        assertEquals("1211", lookAndSay("21"))
        assertEquals("111221", lookAndSay("1211"))
        assertEquals("312211", lookAndSay("111221"))
    }

    @Test
    fun part1() {
        val len = generateSequence("3113322113") { lookAndSay(it) }
            .take(41)
            .last()
            .length

        assertEquals(329356, len)
    }

    @Test
    fun part2() {
        val len = generateSequence("3113322113") { lookAndSay(it) }
            .take(51)
            .last()
            .length

        assertEquals(4666278, len)
    }

    private fun lookAndSay(input: String): String {
        val (r, cc, cnt) = input.fold(Triple(StringBuilder(), null as Char?, 0)) { (acc, currentChar, count), n ->
            if(n == currentChar) {
                Triple(acc, currentChar, count + 1)
            } else {
                if(count == 0) {
                    Triple(acc, n, 1)
                } else {
                    Triple(acc.append(count).append(currentChar), n, 1)
                }
            }
        }

        return r.append(cnt).append(cc).toString()
    }
}