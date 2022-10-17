import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.test.assertEquals

class Day21 {
    val weapons = listOf(
        Item(8, 4, 0),
        Item(10, 5, 0),
        Item(25, 6, 0),
        Item(40, 7, 0),
        Item(74, 8, 0),
    )

    val armors = listOf(
        Item(0, 0, 0),
        Item(13, 0, 1),
        Item(31, 0, 2),
        Item(53, 0, 3),
        Item(75, 0, 4),
        Item(102, 0, 5),
    )

    val rings = listOf(
        Item(0, 0, 0),
        Item(25, 1, 0),
        Item(50, 2, 0),
        Item(100, 3, 0),
        Item(20, 0, 1),
        Item(40, 0, 2),
        Item(80, 0, 3),
    )

    val boss = Entity(100, 8, 2)

    @Test
    fun part1() {
        val minGold = generatePlayers(weapons, armors, rings)
            .filter { p -> fight(p.second, boss) }
            .map { p -> p.first }
            .minOf { i -> i }

        assertEquals(91, minGold)
    }

    @Test
    fun part2() {
        val players = generatePlayers(weapons, armors, rings)
        assertEquals(660, players.toList().size)
        val maxGold = players
            .filter { p -> !fight(p.second, boss) }
            .map { p -> p.first }
            .maxOf { i -> i }

        assertEquals(0, maxGold)
    }

    fun generatePlayers(weapons: List<Item>, armors: List<Item>, rings: List<Item>): Sequence<Pair<Int, Entity>> {
        return weapons.asSequence().flatMap { w ->
            armors.asSequence().flatMap { a ->
                sequence {
                    yield(
                        Pair(
                            w.cost + a.cost,
                            Entity(100, w.dmg + a.dmg, w.armor + a.armor)
                        )
                    )

                    for (i in 1 until rings.size) {
                        val r1 = rings[i]
                        for (j in 0 until i) {
                            val r2 = rings[j]
                            yield(
                                Pair(
                                    w.cost + a.cost + r1.cost + r2.cost,
                                    Entity(
                                        100,
                                        w.dmg + a.dmg + r1.dmg + r2.dmg,
                                        w.armor + a.armor + r1.armor + r2.armor
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun fight(player: Entity, boss: Entity): Boolean {

        val playerDmg = max(player.dmg - boss.armor, 1)
        val bossDmg = max(boss.dmg - player.armor, 1)

        val bossAttacksToKill = (player.hp / bossDmg) + if (player.hp % bossDmg == 0) 0 else 1

        val playerAttacksToKill = boss.hp / playerDmg + if (boss.hp % playerDmg == 0) 0 else 1

        return playerAttacksToKill <= bossAttacksToKill
    }

    data class Entity(val hp: Int, val dmg: Int, val armor: Int)
    data class Item(val cost: Int, val dmg: Int, val armor: Int)
}