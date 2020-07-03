package com.cropo.world.dungeon

import com.cropo.entity.Entity
import com.cropo.entity.EntityBlueprint
import com.cropo.world.dungeon.LayoutElement.*
import com.cropo.world.dungeon.layout.RectangularRoomLayout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import kotlin.random.Random

class DungeonGenerator(private val mapSize: Size) {

    fun generateLevel(): List<Entity> {

        val level = Section(Rect.create(Position.topLeftCorner(), mapSize))
        val rng = Random.Default

        val sections : MutableList<Section> = mutableListOf()

        for (i in 0 until 15) {
            var bounds : Rect
            do {
                val size: Size = Size.create(rng.nextInt(7, 21), rng.nextInt(7, 21))
                val position =
                    Position.create(rng.nextInt(1, mapSize.width - size.width - 1), rng.nextInt(1, mapSize.height - size.height - 1))
                bounds = Rect.create(position, size)
                // Expand the bounds by 1 in each direction - to make sure there's always a wall between
                // the individual rectangular rooms
                val outerBounds = Rect.create(bounds.position.minus(Position.offset1x1()), bounds.size.plus(Size.create(2,2)))
            } while (sections.any { it.bounds.intersects(outerBounds) })

            val section = Section(bounds, RectangularRoomLayout())
            sections.add(section)
            level.merge(section)
        }

        val entities = mutableListOf<Entity>()

        level.layout.forEach { (position: Position, element: LayoutElement) ->
            entities.add(
                when (element) {
                    FLOOR -> EntityBlueprint.floorEntity(position.to3DPosition(0))
                    WALL -> EntityBlueprint.wallEntity(position.to3DPosition(0))
                }
            )

        }
        return entities
    }
}