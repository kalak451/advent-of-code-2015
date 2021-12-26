import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day7 {
    @Test
    fun part1() {
        val testWires = loadWires("day7-test.txt")
        assertEquals(72u, evalGate("d", testWires))
        assertEquals(507u, evalGate("e", testWires))
        assertEquals(492u, evalGate("f", testWires))
        assertEquals(114u, evalGate("g", testWires))
        assertEquals(65412u, evalGate("h", testWires))
        assertEquals(65079u, evalGate("i", testWires))
        assertEquals(123u, evalGate("x", testWires))
        assertEquals(456u, evalGate("y", testWires))

        assertEquals(0u, evalGate("a", loadWires("day7.txt")))
    }

    @Test
    fun part2() {
        val wires = loadWires("day7.txt").toMutableMap()
        val originalA = evalGate("a", wires)
        wires["b"] = Gate(GateType.CONST, originalA, listOf())

        assertEquals(0u, evalGate("a", wires))
    }

    private fun loadWires(path: String): Map<String, Gate> {
        val lines = File(ClassLoader.getSystemResource(path).file).readLines()
        return lines.associate { parse(it) }
    }

    fun evalGate(wireName: String, wires: Map<String, Gate>, answers: MutableMap<String, UShort> = mutableMapOf()): UShort {
        if(answers[wireName] != null) {
            return answers[wireName]!!
        }

        val gate = wires[wireName]!!

        val answer = when (gate.type) {
            GateType.PASS -> evalGate(gate.inputNames[0], wires, answers)
            GateType.CONST -> gate.param!!
            GateType.AND -> if(gate.param == null) {
                evalGate(gate.inputNames[0], wires, answers) and evalGate(gate.inputNames[1], wires, answers)
            } else {
                gate.param and evalGate(gate.inputNames[0], wires, answers)
            }
            GateType.OR -> if(gate.param == null) {
                evalGate(gate.inputNames[0], wires, answers) or evalGate(gate.inputNames[1], wires, answers)
            } else {
                gate.param or evalGate(gate.inputNames[0], wires, answers)
            }
            GateType.LSHIFT -> (evalGate(gate.inputNames[0], wires, answers).toUInt() shl gate.param!!.toInt()).toUShort()
            GateType.RSHIFT -> (evalGate(gate.inputNames[0], wires, answers).toUInt() shr gate.param!!.toInt()).toUShort()
            GateType.NOT -> evalGate(gate.inputNames[0], wires, answers).inv()
        }

        answers[wireName] = answer
        return answer
    }

    private fun parse(line: String): Pair<String, Gate> {
        val mr = "(.*) -> (.*)".toRegex().matchEntire(line)!!

        val output = mr.groupValues[2]

        val rhs = mr.groupValues[1]

        val threeValue = "(.*) (.*) (.*)".toRegex().matchEntire(rhs)
        if (threeValue != null) {
            val left = threeValue.groupValues[1]
            val gateName = GateType.valueOf(threeValue.groupValues[2])
            val right = threeValue.groupValues[3]

            if (setOf(GateType.AND, GateType.OR).contains(gateName)) {
                return if(left.toUShortOrNull() == null) {
                    Pair(output, Gate(gateName, null, listOf(left, right)))
                } else {
                    Pair(output, Gate(gateName, left.toUShort(), listOf(right)))
                }
            }

            return Pair(output, Gate(gateName, right.toUShort(), listOf(left)))
        }

        val twoValue = "(.*) (.*)".toRegex().matchEntire(rhs)
        if (twoValue != null) {
            val gateName = GateType.valueOf(twoValue.groupValues[1])
            val right = twoValue.groupValues[2]

            return Pair(output, Gate(gateName, null, listOf(right)))
        }

        if (rhs.all { it.isDigit() }) {
            return Pair(output, Gate(GateType.CONST, rhs.toUShort(), listOf()))
        } else {
            return Pair(output, Gate(GateType.PASS, null, listOf(rhs)))
        }
    }

    data class Gate(val type: GateType, val param: UShort?, val inputNames: List<String>)

    enum class GateType {
        PASS,
        CONST,
        AND,
        OR,
        LSHIFT,
        RSHIFT,
        NOT
    }
}