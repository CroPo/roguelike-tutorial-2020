package com.cropo.world.dungeon.layout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * This just returns an empty map, and is used as default parameter for the layout strategy.
 * 
 * It is used in situations where a Section needs to created for the sole purpose to merge other sections into it.
 */
class NoLayout : SectionLayoutStrategy{
    override fun generateTerrain(bounds: Rect): Map<Position, LayoutElement> {
        return mapOf()
    }

}