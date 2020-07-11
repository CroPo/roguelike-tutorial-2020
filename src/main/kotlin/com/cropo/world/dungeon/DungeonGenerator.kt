package com.cropo.world.dungeon

import com.cropo.entity.EntityBlueprint
import com.cropo.entity.EntityEngine
import com.cropo.entity.component.GridPosition
import com.cropo.world.dungeon.layout.LayoutElement.*
import com.cropo.world.dungeon.layout.RectangularRoomLayout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import com.cropo.extension.create
import com.cropo.world.dungeon.layout.AllWallsLayout
import com.cropo.world.dungeon.layout.LShapedCorridorLayout
import com.cropo.world.dungeon.layout.LayoutElement
import com.cropo.world.dungeon.spawn.SimpleEnemies
import org.hexworks.cobalt.core.api.UUID
import kotlin.random.Random

class DungeonGenerator(private val mapSize: Size) {

    fun generateLevel(entityEngine: EntityEngine, player: UUID) {

        val rng = Random.Default
        val level = Section(Rect.create(Position.topLeftCorner(), mapSize), rng, entityEngine)
        level.generateLayoutWith(AllWallsLayout())

        val rooms: MutableList<Section> = mutableListOf()

        val roomSizeMin = 6
        val roomSizeMax = 10
        val maxRooms = 30
        val maxMonstersPerRoom = 3

        for (i in 0 until maxRooms) {
            var bounds: Rect
            do {
                val size: Size =
                    Size.create(rng.nextInt(roomSizeMin, roomSizeMax + 1), rng.nextInt(roomSizeMin, roomSizeMax + 1))
                val position =
                    Position.create(
                        rng.nextInt(1, mapSize.width - size.width - 1),
                        rng.nextInt(1, mapSize.height - size.height - 1)
                    )
                bounds = Rect.create(position, size)
                // Expand the bounds by 1 in each direction - to make sure there's always a wall between
                // the individual rectangular rooms
                val outerBounds =
                    Rect.create(bounds.position.minus(Position.offset1x1()), bounds.size.plus(Size.create(2, 2)))
            } while (rooms.any { it.bounds.intersects(outerBounds) })

            val section = Section(bounds, rng, entityEngine)
            section.generateLayoutWith(RectangularRoomLayout())
                .spawnWith(SimpleEnemies(maxMonstersPerRoom))
                .mergeInto(level)

            rooms.add(section)
        }

        entityEngine.get(player, GridPosition::class)!!.position2D = rooms.first().bounds.center

        for (i in 1 until rooms.size) {
            val from = rooms[i].bounds.center
            val to = rooms[i - 1].bounds.center
            val corridor = Section(Rect.create(from, to), rng, entityEngine)
            corridor.generateLayoutWith(LShapedCorridorLayout(from, to))
            level.merge(corridor)
        }


        level.layout.forEach { (position: Position, element: LayoutElement) ->
            when (element) {
                FLOOR -> EntityBlueprint.floorEntity(entityEngine, position.toPosition3D(0))
                WALL -> EntityBlueprint.wallEntity(entityEngine, position.toPosition3D(0))
            }
        }
    }
}