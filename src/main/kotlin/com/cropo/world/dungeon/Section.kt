package com.cropo.world.dungeon

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * A section of the map on defined position with a defined size,
 * containing and managing the layout of this section
 */
class Section(
    val bounds: Rect
) {
    private val internalSection: MutableMap<Position, LayoutElement> = HashMap()

    /**
     * Each available [Position] mapped to the [LayoutElement] which describes the content
     */
    val layout: Map<Position, LayoutElement>
        get() = internalSection

    init {
        bounds.fetchPositions().forEach {
            internalSection[it] = LayoutElement.WALL
        }
    }
}