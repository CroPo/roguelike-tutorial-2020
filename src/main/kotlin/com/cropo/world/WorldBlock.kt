package com.cropo.world

import com.cropo.entity.component.GridTile
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock

/**
 * A slightly modified version of the Zircon `Block` which holds a List of `Entity`. These are used to determin
 * which tile is displayed on which position.
 */

class WorldBlock(private val entities: MutableList<Entity> = mutableListOf()) :
    BaseBlock<Tile>(Tile.empty(), persistentMapOf()) {

    var isVisible = false

    private val exploredEmptyTile: Tile?
        get() = entities.firstOrNull { it.isExplored && it.type == EntityType.TERRAIN }?.tileExplored

    override val emptyTile: Tile
        get() =
            when {
                !isVisible -> exploredEmptyTile ?: super.emptyTile
                entities.any { it.type == EntityType.TERRAIN } -> entities.first { it.type == EntityType.TERRAIN }.tile
                else -> super.emptyTile
            }

    override var tiles: PersistentMap<BlockTileType, Tile>
        get() = persistentMapOf(
            Pair(
                BlockTileType.TOP, when {
                    !isVisible -> emptyTile
                    entities.isEmpty() -> emptyTile
                    else -> entities.first().tile
                }
            )
        )
        set(value) {}

    fun addEntity(entity: UUID) {
        if(!entity.components.any { it is GridTile }) {
            return
        }

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