package com.cropo.entity

import com.cropo.entity.component.Component
import com.cropo.entity.component.MapAttributes
import com.cropo.entity.component.Position
import com.cropo.entity.component.Tile
import com.cropo.tile.TileBlueprint
import org.hexworks.zircon.api.data.Position3D

object EntityBlueprint {

    fun wallEntity(position: Position3D): Entity {
        return Entity(
            mutableListOf(
                Position(position),
                Tile(
                    tileVisible = TileBlueprint.wall(),
                    tileHidden = TileBlueprint.wallExplored()
                ),
                MapAttributes(
                    isBlocking = true
                )
            )
        )
    }

    fun floorEntity(position: Position3D): Entity {
        return Entity(
            mutableListOf(
                Position(position),
                Tile(
                    tileVisible = TileBlueprint.floor(),
                    tileHidden = TileBlueprint.floorExplored()
                ),
                MapAttributes(
                    isTransparent = true
                )
            )
        )
    }
}