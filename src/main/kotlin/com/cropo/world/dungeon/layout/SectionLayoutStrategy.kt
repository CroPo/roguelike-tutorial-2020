package com.cropo.world.dungeon.layout

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

interface SectionLayoutStrategy {

    /**
     * Generate the terrain for the selected Layout
     *
     * Walls don't need to be generated, because they are already present in the [Section]
     */
    fun generateTerrain(bounds: Rect) : Map<Position, LayoutElement>
}