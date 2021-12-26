import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14 {
    @Test
    fun part1() {
        val deer = loadData()

        val max = deer.map { it.take(2503).sum() }.maxOf { it }

        assertEquals(2640, max)
    }

    @Test
    fun testPart2() {
        val deer = loadTestData()

        val x = scoringV2(deer, 1000)

        assertEquals(689, x)
    }

    @Test
    fun part2() {
        val deer = loadData()

        val x = scoringV2(deer, 2503)

        assertEquals(1102, x)
    }

    private fun scoringV2(deer: List<Sequence<Int>>, duration: Int): Int {
        val runningDistances = deer.map { it.runningFold(0) { acc, i -> acc + i }.take(duration + 1).toList() }

        return (1..duration).flatMap { s ->
                val scores = runningDistances.withIndex().groupBy { it.value[s] }
                val maxScore = scores.keys.maxOf { it }
                scores[maxScore]!!.map { it.index }
            }.groupBy { it }.maxByOrNull { it.value.size }!!.value.count()
    }

    private fun loadTestData(): List<Sequence<Int>> {
        val deer = listOf(
            genSeq(14, 10, 127), genSeq(16, 11, 162)
        )
        return deer
    }

    private fun loadData(): List<Sequence<Int>> {
        val deer = listOf(
            genSeq(27, 5, 132),
            genSeq(22, 2, 41),
            genSeq(11, 5, 48),
            genSeq(28, 5, 134),
            genSeq(4, 16, 55),
            genSeq(14, 3, 38),
            genSeq(3, 21, 40),
            genSeq(18, 6, 103),
            genSeq(18, 5, 84),
        )
        return deer
    }

    private fun genSeq(runningSpeed: Int, runningSeconds: Int, restingSeconds: Int): Sequence<Int> {
        val running = (1..runningSeconds).map { runningSpeed }
        val resting = (1..restingSeconds).map { 0 }
        val combined = running + resting

        return generateSequence(combined) { _ -> combined }.flatten()
    }
}