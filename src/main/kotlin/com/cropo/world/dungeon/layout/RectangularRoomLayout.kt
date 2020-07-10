package com.cropo.world.dungeon.layout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * This layout strategy describes a rectangular room
 */
class RectangularRoomLayout : SectionLayoutStrategy {
    override fun generateTerrain(bounds: Rect) : Map<Position, LayoutElement> {
        val terrain = mutableMapOf<Position, LayoutElement>()
        bounds.fetchPositions().forEach {
            terrain[it] = LayoutElement.FLOOR
        }
        return terrain
    }
}