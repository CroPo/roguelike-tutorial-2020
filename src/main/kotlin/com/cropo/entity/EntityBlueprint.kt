package com.cropo.entity

import com.cropo.tile.TileBlueprint
import org.hexworks.zircon.api.data.Position3D

object EntityBlueprint {

    fun wallEntity(position: Position3D): Entity {
        return Entity(
            position, EntityType.TERRAIN,
            tile = TileBlueprint.wall(),
            tileExplored = TileBlueprint.wallExplored(),
            isWalkable = false,
            isTransparent = false
        )
    }

    fun floorEntity(position: Position3D): Entity {
        return Entity(
            position, EntityType.TERRAIN,
            tile = TileBlueprint.floor(),
            tileExplored = TileBlueprint.floorExplored()
        )
    }
}