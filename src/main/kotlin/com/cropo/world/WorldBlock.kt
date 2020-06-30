package com.cropo.world

import com.cropo.entity.Entity
import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.zircon.api.builder.data.BlockBuilder
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import org.hexworks.zircon.internal.data.DefaultBlock

/**
 * A slightly modified version of the Zircon `Block` which holds a List of `Entity`. These define the
 * actual Tile of the Block.
 */

class WorldBlock(emptyTile: Tile = Tile.empty(), private val entities: MutableList<Entity> = mutableListOf()) :
    BaseBlock<Tile>(emptyTile, persistentMapOf()) {

    override fun createCopy(): Block<Tile> {
        return WorldBlock(emptyTile)
    }
}