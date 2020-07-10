package com.cropo.world

import com.cropo.entity.EntityEngine
import com.cropo.entity.component.GridAttributes
import com.cropo.entity.component.GridTile
import com.cropo.tile.TileLayer
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

class WorldBlock(private val entityEngine: EntityEngine) :
    BaseBlock<Tile>(Tile.empty(), persistentMapOf()) {

    private val entities: MutableList<UUID> = mutableListOf()

    var isVisible = false

    private val terrainLayerEntities: List<UUID>
        get() = entities.filter {
            entityEngine.has(it, GridTile::class)
                    && entityEngine.get(it, GridTile::class)!!.layer == TileLayer.TERRAIN
        }

    private val exploredEmptyTile: Tile?
        get() =
            terrainLayerEntities.filter {
                entityEngine.has(it, GridAttributes::class)
                        && entityEngine.get(it, GridAttributes::class)!!.isExplored
            }.map {
                entityEngine.get(it, GridTile::class)!!.tileHidden
            }.firstOrNull()


    override val emptyTile: Tile
        get() =
            when {
                !isVisible -> exploredEmptyTile ?: super.emptyTile
                terrainLayerEntities.isNotEmpty() -> terrainLayerEntities.map {
                    entityEngine.get(it, GridTile::class)!!.tileVisible
                }.first()
                else -> super.emptyTile
            }

    override var tiles: PersistentMap<BlockTileType, Tile>
        get() = persistentMapOf(
            Pair(
                BlockTileType.TOP, when {
                    !isVisible || entities.isEmpty() -> emptyTile
                    else -> entities.filter {
                        entityEngine.has(it, GridTile::class)
                    }.map {
                        entityEngine.get(it, GridTile::class)!!.tileVisible
                    }.first()
                }
            )
        )
        set(value) {}

    /**
     * Add an entity to this block. If the entity doesn't have the [GridTile] component, it won't be added.
     */
    fun addEntity(entity: UUID) {
        if(!entityEngine.has(entity, GridTile::class)) {
            return
        }
        entities.add(entity)
        entities.sortBy {
            entityEngine.get(it, GridTile::class)?.layer
        }
    }

    /**
     * Removes an entity from this block.
     */
    fun removeEntity(entity: UUID) {
        entities.remove(entity)
    }

    /**
     * Get an (immutable) [List] of all entities which are present on this block
     */
    fun getEntityList() : List<UUID> {
        return entities
    }

    override fun createCopy(): Block<Tile> {
        return WorldBlock(entityEngine)
    }
}