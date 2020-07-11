package com.cropo.world.dungeon.spawn

import com.cropo.entity.EntityEngine
import com.cropo.world.dungeon.layout.LayoutElement
import org.hexworks.zircon.api.data.Position
import kotlin.random.Random

class SimpleEnemies(private val maxMonsters: Int) : SpawnStrategy {
    override fun spawn(
        rng: Random,
        entityEngine: EntityEngine,
        terrain: Map<Position, LayoutElement>
    ) {
        val numberOfMonsters = rng.nextInt(maxMonsters+1)



    }
}