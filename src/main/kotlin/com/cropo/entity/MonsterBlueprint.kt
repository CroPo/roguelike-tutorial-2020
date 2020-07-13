package com.cropo.entity

import com.cropo.entity.component.*
import com.cropo.tile.TileBlueprint
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.data.Position3D


object MonsterBlueprint {

    fun orc(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                Actor(),
                GridPosition(position),
                Name(
                    name = "Orc",
                    description = "A regular greenskin"
                ),
                GridTile(
                    tileVisible = TileBlueprint.orc(),
                    isTransparent = true,
                    isBlocking = true
                )
            )
        ).build()
    }

    fun troll(engine: EntityEngine, position: Position3D): UUID {
        return EntityBuilder.createBuilder(engine).with(
            setOf(
                Actor(),
                GridPosition(position),
                Name(
                    name = "Troll",
                    description = "Big, hairy and hungry"
                ),
                GridTile(
                    tileVisible = TileBlueprint.troll(),
                    isTransparent = true,
                    isBlocking = true
                )
            )
        ).build()
    }

}