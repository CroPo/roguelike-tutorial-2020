package com.cropo.tile

import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

object TileBlueprint {
    fun wall(): Tile {
        return Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(TileColor.create(200,200,200))
            .withBackgroundColor(TileColor.create(160,150,150))
            .build()
    }

    fun floor(): Tile {
        return Tile.newBuilder()
            .withCharacter('.')
            .withForegroundColor(TileColor.create(70,70,70))
            .withBackgroundColor(TileColor.create(50,50,50))
            .build()
    }
}