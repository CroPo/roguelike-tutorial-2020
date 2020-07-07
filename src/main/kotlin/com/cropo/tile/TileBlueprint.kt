package com.cropo.tile

import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

object TileBlueprint {
    fun wall(): Tile {
        return Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(TileColor.create(60,60,60))
            .withBackgroundColor(TileColor.create(100,100,90))
            .build()
    }
    fun wallExplored(): Tile {
        return wall()
            .withForegroundColor(TileColor.create(20,20,20))
            .withBackgroundColor(TileColor.create(40,35,35))
    }

    fun floor(): Tile {
        return Tile.newBuilder()
            .withCharacter('.')
            .withForegroundColor(TileColor.create(70,70,70))
            .withBackgroundColor(TileColor.create(50,50,50))
            .build()
    }
    fun floorExplored(): Tile {
        return floor()
            .withForegroundColor(TileColor.create(25,25,25))
            .withBackgroundColor(TileColor.create(15,15,15))
    }
}