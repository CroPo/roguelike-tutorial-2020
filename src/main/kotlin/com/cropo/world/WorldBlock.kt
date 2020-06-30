package com.cropo.world

import com.cropo.entity.Entity
import com.cropo.entity.EntityType
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock

/**
 * A slightly modified version of the Zircon `Block` which holds a List of `Entity`. These are used to determin
 * which tile is displayed on which position.
 */

class WorldBlock(private val entities: MutableList<Entity> = mutableListOf()) :
    BaseBlock<Tile>(Tile.empty(), persistentMapOf()) {

    override val emptyTile: Tile
        get() =
            when {
                entities.any { it.type == EntityType.TERRAIN } -> entities.first { it.type == EntityType.TERRAIN }.tile
                else -> Tile.empty()
            }


    fun addEntity(entity: Entity) {
        entities.add(entity)
        entities.sortBy { it.type }
    }

    fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }

    override fun createCopy(): Block<Tile> {
        return WorldBlock()
    }
}