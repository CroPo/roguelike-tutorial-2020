package com.cropo.entity

import com.cropo.entity.component.GridAttributes
import com.cropo.entity.component.GridPosition
import com.cropo.entity.component.GridTile
import com.cropo.tile.TileBlueprint
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.data.Position3D

object EntityBlueprint {

    fun wallEntity(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                GridPosition(position),
                GridTile(
                    tileVisible = TileBlueprint.wall(),
                    tileHidden = TileBlueprint.wallExplored()
                ),
                GridAttributes(
                    isBlocking = true
                )
            )
        ).build()
    }

    fun floorEntity(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                GridPosition(position),
                GridTile(
                    tileVisible = TileBlueprint.floor(),
                    tileHidden = TileBlueprint.floorExplored()
                ),
                GridAttributes(
                    isTransparent = true
                )
            )
        ).build()
    }
}