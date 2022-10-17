import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.test.assertEquals

class Day22 {
    var cache: MutableMap<GameState, Long?> = mutableMapOf()

    @Test
    fun sample1() {
        val gs = GameState(10, 13, 8, 250, 0, 0, 0, 0, true)
        val moves: List<(GameState) -> GameState> = listOf(
            this::poisonMove,
            this::bossMove,
            this::missleMove,
            this::bossMove
        )

        val result = moves.fold(gs) { acc: GameState, m: (GameState) -> GameState ->
            val init = initTurn(acc)
            if (init.player <= 0 || init.boss <= 0) {
                init
            } else {
                m.invoke(init)
            }
        }

        assertEquals(0, result.boss)
    }

    @Test
    fun part1() {
        val gs = GameState(50, 59, 9, 500, 0, 0, 0, 0, true)
        val m = fight(gs, this::initTurn)

        assertEquals(1269, m)
    }

    @Test
    fun part2() {
        val gs = GameState(50, 59, 9, 500, 0, 0, 0, 0, true)
        val m = fight(gs, this::initTurnWithPenalty)

        assertEquals(0, m)
    }

    fun loadCache(gs: GameState, value: Long?): Long? {
        cache.put(gs, value)
        return value
    }

    fun fight(initGs: GameState, initF: (GameState) -> GameState): Long? {
        if (cache.contains(initGs)) {
            return cache.get(initGs)
        }

        val gs = initF.invoke(initGs)

        if (gs.boss <= 0) {
            return loadCache(initGs, 0);
        }

        if (gs.player <= 0) {
            return loadCache(initGs, null);
        }

        val moves = sequence {
            if (gs.playerTurn) {
                if (gs.mana >= 53) {
                    yield(Pair(53, missleMove(gs)))
                }

                if (gs.mana >= 73) {
                    yield(Pair(73, drainMove(gs)))
                }

                if (gs.mana >= 113 && gs.shieldCtr == 0) {
                    yield(Pair(113, shieldMove(gs)))
                }

                if (gs.mana >= 173 && gs.poisonCtr == 0) {
                    yield(Pair(173, poisonMove(gs)))
                }

                if (gs.mana >= 229 && gs.rechargeCtr == 0) {
                    yield(Pair(229, rechargeMove(gs)))
                }
            } else {
                yield(Pair(0, bossMove(gs)))
            }

        }.toList()

        if (moves.isEmpty()) {
            return loadCache(initGs, null);
        }

        val minMove = moves.filter { g -> g.second.boss <= 0 }.map { g -> g.first }.minByOrNull { c -> c }

        if (minMove != null) {
            return loadCache(initGs, minMove.toLong())
        }

        val result = moves.filter { g -> g.second.player > 0 }.map { (current, g) ->
            val next = fight(g, initF)
            if (next == null) {
                null
            } else {
                current + next
            }
        }.filterNotNull().minOfOrNull { x -> x }

        return loadCache(initGs, result)
    }

    private fun bossMove(gs: GameState) = GameState(
        gs.player - (gs.bossDmg - gs.armor),
        gs.boss,
        gs.bossDmg,
        gs.mana,
        gs.armor,
        gs.shieldCtr,
        gs.poisonCtr,
        gs.rechargeCtr,
        true
    )

    private fun rechargeMove(gs: GameState) = GameState(
        gs.player, gs.boss, gs.bossDmg, gs.mana - 229, gs.armor, gs.shieldCtr, gs.poisonCtr, 5, false
    )

    private fun poisonMove(gs: GameState) = GameState(
        gs.player, gs.boss, gs.bossDmg, gs.mana - 173, gs.armor, gs.shieldCtr, 6, gs.rechargeCtr, false
    )

    private fun shieldMove(gs: GameState) = GameState(
        gs.player, gs.boss, gs.bossDmg, gs.mana - 113, gs.armor, 6, gs.poisonCtr, gs.rechargeCtr, false
    )

    private fun drainMove(gs: GameState) = GameState(
        2 + gs.player,
        gs.boss - 2,
        gs.bossDmg,
        gs.mana - 73,
        gs.armor,
        gs.shieldCtr,
        gs.poisonCtr,
        gs.rechargeCtr,
        false
    )

    private fun missleMove(gs: GameState) = GameState(
        gs.player, gs.boss - 4, gs.bossDmg, gs.mana - 53, gs.armor, gs.shieldCtr, gs.poisonCtr, gs.rechargeCtr, false
    )

    fun initTurnWithPenalty(gs: GameState): GameState {
        if (gs.playerTurn) {
            val gs2 = GameState(
                gs.player - 1,
                gs.boss,
                gs.bossDmg,
                gs.mana,
                gs.armor,
                gs.shieldCtr,
                gs.poisonCtr,
                gs.rechargeCtr,
                gs.playerTurn
            )
            if (gs2.player <= 0) {
                return gs2
            }

            return initTurn(gs2)
        } else {
            return initTurn(gs)
        }

    }

    fun initTurn(gs: GameState): GameState {
        var p = gs.player
        var b = gs.boss
        var m = gs.mana
        var a: Int

        if (gs.shieldCtr > 0) {
            a = 7
        } else {
            a = 0
        }

        if (gs.poisonCtr > 0) {
            b -= 3
        }

        if (gs.rechargeCtr > 0) {
            m += 101
        }

        return GameState(
            p,
            b,
            gs.bossDmg,
            m,
            a,
            max(0, gs.shieldCtr - 1),
            max(0, gs.poisonCtr - 1),
            max(0, gs.rechargeCtr - 1),
            gs.playerTurn
        )
    }

    data class GameState(
        val player: Int,
        val boss: Int,
        val bossDmg: Int,
        val mana: Long,
        val armor: Int,
        val shieldCtr: Int,
        val poisonCtr: Int,
        val rechargeCtr: Int,
        val playerTurn: Boolean
    )
}
