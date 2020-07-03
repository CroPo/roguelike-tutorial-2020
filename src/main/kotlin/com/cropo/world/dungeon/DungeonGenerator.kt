package com.cropo.world.dungeon

import com.cropo.entity.Entity
import com.cropo.entity.EntityBlueprint
import com.cropo.world.dungeon.LayoutElement.*

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size

class DungeonGenerator(private val mapSize: Size) {

    private var level = Section(Rect.create(Position.topLeftCorner(), mapSize))

    fun generateLevel(): List<Entity> {



        val room = mutableListOf<Entity>()

        level.layout.forEach { (position: Position, element: LayoutElement) ->
            room.add(
                when (element) {
                    FLOOR -> EntityBlueprint.floorEntity(position.to3DPosition(0))
                    WALL -> EntityBlueprint.wallEntity(position.to3DPosition(0))
                }
            )

        }
        return room
    }
}