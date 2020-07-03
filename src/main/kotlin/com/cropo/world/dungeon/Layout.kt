package com.cropo.world.dungeon

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * The layout of a single dungeon level, or a smaller section of it
 */
class Layout(
    private val bounds: Rect
) {
    private val internalTerrain: MutableMap<Position, LayoutElement> = HashMap()

    /**
     * Each available [Position] mapped to the [LayoutElement] which describes the content
     */
    val terrain : Map<Position, LayoutElement>
    get() = internalTerrain

    init {
        bounds.fetchPositions().forEach {
            internalTerrain[it] = LayoutElement.WALL
        }
    }
}