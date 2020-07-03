package com.cropo.generator

import com.cropo.entity.Entity
import com.cropo.entity.EntityBlueprint
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size

class DungeonGenerator(val bounds: Size) {

    fun generateLevel(): List<Entity> {
        val offset = Position.create(35, 20);
        val size = Size.create(11, 7)
        val innerSize = size.minus(Size.create(2, 2))

        val room = MutableList(size.width * size.height) { i ->
            val x = i / size.height
            val y = i % size.height

            EntityBlueprint.wallEntity(Position3D.create(x + offset.x, y + offset.y, 0))
        }

        for (x in 0 until innerSize.width) {
            val iOffset = x * size.height + size.height + 1

            for (y in 0 until innerSize.height) {
                val i = iOffset + y
                room[i] = EntityBlueprint.floorEntity(Position3D.create(x + offset.x + 1, y + offset.y + 1, 0))
            }
        }

        return room
    }
}