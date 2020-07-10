package com.cropo.entity

import com.cropo.entity.component.GridAttributes
import com.cropo.entity.component.GridPosition
import com.cropo.entity.component.GridTile
import com.cropo.entity.component.Name
import com.cropo.tile.TileBlueprint
import com.cropo.tile.TileLayer
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.data.Position3D


object MonsterBlueprint {

    fun orc(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                GridPosition(position),
                Name(
                    name = "Orc",
                    description = "A regular greenskin"
                ),
                GridTile(
                    tileVisible = TileBlueprint.orc(),
                    layer = TileLayer.ACTOR
                ),
                GridAttributes(
                    isTransparent = true,
                    isBlocking = true
                )
            )
        ).build()
    }

    fun troll(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                GridPosition(position),
                Name(
                    name = "Troll",
                    description = "Big, hairy and hungry"
                ),
                GridTile(
                    tileVisible = TileBlueprint.troll(),
                    layer = TileLayer.ACTOR
                ),
                GridAttributes(
                    isTransparent = true,
                    isBlocking = true
                )
            )
        ).build()
    }

}