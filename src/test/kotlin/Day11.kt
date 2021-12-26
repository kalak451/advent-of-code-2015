import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11 {

    @Test
    fun part1() {
        val result = generateSequence(increment("hepxcrrq")) { increment(it) }
            .filter { passesRules(it) }
            .first()

        assertEquals("hepxxyzz", result)
    }

    @Test
    fun part2() {
        val result = generateSequence(increment("hepxcrrq")) { increment(it) }
            .filter { passesRules(it) }
            .drop(1)
            .first()

        assertEquals("heqaabcc", result)
    }

    @Test
    fun shouldIncrement() {
        assertEquals("b", increment("a"))
        assertEquals("xy", increment("xx"))
        assertEquals("xz", increment("xy"))
        assertEquals("ya", increment("xz"))
    }

    fun increment(s: String): String {
        val (sb, carry) = s.foldRight((Pair(StringBuilder(), true))) { c, (sb, carry) ->
            if(carry) {
                if(c == 'z') {
                    Pair(sb.append('a'), true)
                } else {
                    Pair(sb.append(c + 1), false)
                }
            } else {
                Pair(sb.append(c), false)
            }
        }

        return sb.reverse().toString()
    }

    @Test
    fun shouldRules() {
        assertEquals(false, passesRules("hijklmmn"))
        assertEquals(false, passesRules("abbceffg"))
        assertEquals(false, passesRules("abbcegjk"))
        assertEquals(true, passesRules("abcdffaa"))
        assertEquals(true, passesRules("ghjaabcc"))
    }

    fun passesRules(s: String): Boolean {
        val r1 = rule1(s)
        val r2 = rule2(s)
        val r3 = rule3(s)

        return r1 && r2 && r3
    }

    @Test
    fun shouldRule3() {
        assertEquals(false, rule3("aa"))
        assertEquals(false, rule3("aaa"))
        assertEquals(true, rule3("aaaa"))
        assertEquals(true, rule3("aabcdefgaa"))
        assertEquals(true, rule3("abcdffaa"))
    }

    private val rule3Regex = ".*(.)\\1.*(.)\\2.*".toRegex()
    fun rule3(s: String): Boolean {
        return rule3Regex.matches(s)
    }

    val rule2Chars = setOf('i', 'o', 'l')
    fun rule2(s: String): Boolean {
        return !s.any { rule2Chars.contains(it) }
    }

    fun rule1(s: String): Boolean {
        return s.windowed(3, 1)
            .map { it.toList() }
            .any { (a, b, c) -> a + 1 == b && b + 1 == c }
    }
}