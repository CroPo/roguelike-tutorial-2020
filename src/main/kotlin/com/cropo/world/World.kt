package com.cropo.world

import com.cropo.entity.Entity
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea

class World(
    actualSize: Size3D,
    visibleSize: Size3D,
    entities: MutableList<Entity>,
    entityBlueprint: (Position3D) -> Entity
) : BaseGameArea<Tile, WorldBlock>(actualSize, visibleSize) {
    init {
        actualSize.fetchPositions().forEach { position ->
            val entity = entityBlueprint(position)
            val block = WorldBlock()
            block.addEntity(entity)
            entities.add(entity)
            setBlockAt(position, block)
        }
    }
}