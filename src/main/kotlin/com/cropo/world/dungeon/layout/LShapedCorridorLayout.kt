package com.cropo.world.dungeon.layout

import com.cropo.extension.create
import com.cropo.world.dungeon.layout.LayoutElement.*
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import kotlin.random.Random

/**
 * Generate an L-shaped corridor
 */
class LShapedCorridorLayout(
    private val rng: Random,
    private val from: Position,
    private val to: Position
) : SectionLayoutStrategy {
    override fun generateTerrain(bounds: Rect): Map<Position, LayoutElement> {
        val terrain = mutableMapOf<Position, LayoutElement>()

        val corner = if (rng.nextBoolean()) {
            Position.create(from.x, to.y)
        } else {
            Position.create(to.x, from.y)
        }

        terrain.putAll(generateCorridorPart(from, corner))
        terrain.putAll(generateCorridorPart(corner, to))

        return terrain
    }

    /**
     * Generate an individual, straight part of the corridor from one position to the other.
     */
    private fun generateCorridorPart(
        from: Position, to: Position
    ): Map<Position, LayoutElement> {
        val corridor = mutableMapOf<Position, LayoutElement>()

        Rect.create(from, to).fetchPositions().forEach {
            corridor[it] = FLOOR
        }
        return corridor
    }
}