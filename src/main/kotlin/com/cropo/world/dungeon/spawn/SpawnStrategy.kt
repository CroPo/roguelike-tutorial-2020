package com.cropo.world.dungeon.spawn

import com.cropo.entity.EntityEngine
import com.cropo.world.dungeon.layout.LayoutElement
import org.hexworks.zircon.api.data.Position

interface SpawnStrategy {
    /**
     * Spawn a set of objects in the given area.
     */
    fun spawn(entityEngine: EntityEngine, terrain: Map<Position, LayoutElement>)
}