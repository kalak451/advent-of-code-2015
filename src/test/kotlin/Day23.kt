import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day23 {
    val parseRegex = "^([a-z]{3}) (.+?)(?:, (.*))?$".toRegex()

    data class Instruction(val inst: String, val register: String?, val offset: Int?)

    fun parse(program: String): List<Instruction> {
        val programLines = File(ClassLoader.getSystemResource(program).file).readLines()
        return parse(programLines)
    }

    fun parse(program: List<String>): List<Instruction> {
        return program
            .mapNotNull { l -> parseRegex.matchEntire(l) }
            .map { m ->
                when (val inst = m.groups[1]?.value!!) {
                    "hlf", "tpl", "inc" -> {
                        Instruction(inst, m.groups[2]?.value, null)
                    }

                    "jmp" -> {
                        Instruction(inst, null, m.groups[2]?.value?.toInt())
                    }

                    "jio", "jie" -> {
                        Instruction(inst, m.groups[2]?.value, m.groups[3]?.value?.toInt())
                    }

                    else -> {
                        throw Exception("Error parsing instruction: " + m.value)
                    }
                }
            }
    }

    fun run(program: List<Instruction>, initA: Int = 0, initB: Int = 0): Pair<Int, Int> {
        val registers = mutableMapOf<String, Int>()
        registers["a"] = initA
        registers["b"] = initB

        var pc = 0

        while (pc < program.size) {
            val inst = program[pc]
            pc = when (inst.inst) {
                "hlf" -> {
                    registers[inst.register!!] = registers[inst.register]!! / 2
                    pc + 1
                }

                "tpl" -> {
                    registers[inst.register!!] = registers[inst.register]!! * 3
                    pc + 1
                }

                "inc" -> {
                    registers[inst.register!!] = registers[inst.register]!! + 1
                    pc + 1
                }

                "jmp" -> {
                    pc + inst.offset!!
                }

                "jie" -> {
                    val regValue = registers[inst.register!!]!!
                    if (regValue % 2 == 0) {
                        pc + inst.offset!!
                    } else {
                        pc + 1
                    }
                }

                "jio" -> {
                    val regValue = registers[inst.register!!]!!
                    if (regValue == 1) {
                        pc + inst.offset!!
                    } else {
                        pc + 1
                    }
                }

                else -> {
                    throw Exception("Invalid Instruction: $inst")
                }
            }
        }

        return Pair(registers["a"]!!, registers["b"]!!)
    }

    @Test
    fun part1() {
        val program = parse("day23-1.txt")

        assertEquals(47, program.size)

        val results = run(program)

        assertEquals(307, results.second)
    }

    @Test
    fun part2() {
        val program = parse("day23-1.txt")

        assertEquals(47, program.size)

        val results = run(program, 1)

        assertEquals(-1, results.second)
    }

}