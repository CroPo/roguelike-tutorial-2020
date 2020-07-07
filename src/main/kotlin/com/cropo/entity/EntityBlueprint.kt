package com.cropo.entity

import com.cropo.tile.TileBlueprint
import org.hexworks.zircon.api.data.Position3D

object EntityBlueprint {

    fun wallEntity(position: Position3D): Entity {
        return Entity(position, EntityType.TERRAIN, TileBlueprint.wall(), isWalkable = false, isTransparent = false)
    }

    fun floorEntity(position: Position3D): Entity {
        return Entity(position, EntityType.TERRAIN, TileBlueprint.floor())
    }
}