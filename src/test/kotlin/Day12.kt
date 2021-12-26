import kotlinx.serialization.json.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class Day12 {
    @Test
    fun part1() {
        val results = loadFile("day12.txt")
        assertNotNull(results)

        val numbers = collectNumbers(results)

        assertEquals(191164, numbers.sum())
    }

    @Test
    fun part2() {
        val results = loadFile("day12.txt")
        assertNotNull(results)

        val numbers = collectNumbers2(results)

        assertEquals(191164, numbers.sum())
    }

    private fun collectNumbers(element: JsonElement): List<Int> {
        return when(element) {
            is JsonObject -> {
                element.values.flatMap { collectNumbers(it) }
            }
            is JsonArray -> {
                element.flatMap { collectNumbers(it) }
            }
            is JsonPrimitive -> {
                if(element.isString) {
                    listOf()
                } else {
                    listOf(element.int)
                }
            }
        }
    }

    private fun collectNumbers2(element: JsonElement): List<Int> {
        return when(element) {
            is JsonObject -> {
                if(element.values.filterIsInstance<JsonPrimitive>().any { it.content == "red"}) {
                    listOf()
                } else {
                    element.values.flatMap { collectNumbers2(it) }
                }
            }
            is JsonArray -> {
                element.flatMap { collectNumbers2(it) }
            }
            is JsonPrimitive -> {
                if(element.isString) {
                    listOf()
                } else {
                    listOf(element.int)
                }
            }
        }
    }

    fun loadFile(path: String): JsonElement {
        return Json.decodeFromStream<JsonElement>(File(ClassLoader.getSystemResource(path).file).inputStream())
    }
}