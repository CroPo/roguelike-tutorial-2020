package com.cropo.world

import com.cropo.entity.EntityEngine
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea

class World(
    actualSize: Size3D,
    visibleSize: Size3D,
    entityEngine: EntityEngine
) : BaseGameArea<Tile, WorldBlock>(actualSize, visibleSize) {
    init {
        actualSize.fetchPositions().forEach { position ->
            setBlockAt(position, WorldBlock(entityEngine))
        }
    }

    /**
     * Set all [WorldBlock]s within the FOV to visible
     */
    fun updateFov(visiblePositions: List<Position3D>) {
        blocks.forEach { (_, block) -> block.isVisible = false }
        visiblePositions.forEach { position ->
            blocks[position]?.isVisible = true
        }
    }
}