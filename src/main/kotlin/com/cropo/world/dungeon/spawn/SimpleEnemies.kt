package com.cropo.world.dungeon.spawn

import com.cropo.entity.EntityBlueprint
import com.cropo.entity.EntityEngine
import com.cropo.entity.MonsterBlueprint
import com.cropo.world.dungeon.layout.LayoutElement
import org.hexworks.zircon.api.data.Position
import kotlin.math.max
import kotlin.random.Random

class SimpleEnemies(private var maxMonsters: Int) : SpawnStrategy {
    override fun spawn(
        rng: Random,
        entityEngine: EntityEngine,
        layout: Map<Position, LayoutElement>
    ) {
        val availablePositions =
            layout.filterValues { layoutElement -> layoutElement == LayoutElement.FLOOR }.keys.toList()

        if(availablePositions.size > maxMonsters){
            maxMonsters = availablePositions.size
        }

        val numberOfMonsters = rng.nextInt(maxMonsters + 1)
        val occupiedPositions: MutableList<Position> = mutableListOf()

        while (occupiedPositions.size < numberOfMonsters) {
            val position = availablePositions[rng.nextInt(availablePositions.size)]
            if (occupiedPositions.contains(position)) {
                continue;
            }
            if (rng.nextInt(10) > 7) {
                MonsterBlueprint.troll(entityEngine, position.to3DPosition(0))
            } else {
                MonsterBlueprint.orc(entityEngine, position.to3DPosition(0))
            }
            occupiedPositions.add(position)
        }
    }
}