package com.cropo.tile

import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

object TileBlueprint {
    fun wall(): Tile {
        return Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(TileColor.create(60, 65, 60))
            .withBackgroundColor(TileColor.create(90, 100, 90))
            .build()
    }

    fun wallExplored(): Tile {
        return wall()
            .withForegroundColor(TileColor.create(20, 22, 20))
            .withBackgroundColor(TileColor.create(35, 40, 35))
    }

    fun floor(): Tile {
        return Tile.newBuilder()
            .withCharacter('.')
            .withForegroundColor(TileColor.create(70, 70, 70))
            .withBackgroundColor(TileColor.create(50, 50, 50))
            .build()
    }

    fun floorExplored(): Tile {
        return floor()
            .withForegroundColor(TileColor.create(25, 25, 25))
            .withBackgroundColor(TileColor.create(15, 15, 15))
    }

    fun player(): Tile {
        return Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.defaultForegroundColor())
            .build()
    }

    fun orc(): Tile {
        return Tile.newBuilder()
            .withCharacter('o')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.create(33, 100, 33))
            .build()
    }

    fun troll(): Tile {
        return Tile.newBuilder()
            .withCharacter('T')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.create(10, 100, 10))
            .build()
    }
}