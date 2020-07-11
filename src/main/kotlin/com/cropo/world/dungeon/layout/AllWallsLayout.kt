package com.cropo.world.dungeon.layout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * Fill every available position with a [LayoutElement.WALL]
 */
class AllWallsLayout : SectionLayoutStrategy {
    override fun generateTerrain(bounds: Rect): Map<Position, LayoutElement> {
        val terrain = mutableMapOf<Position, LayoutElement>()
        bounds.fetchPositions().forEach {
            terrain[it] = LayoutElement.WALL
        }
        return terrain;
    }
}