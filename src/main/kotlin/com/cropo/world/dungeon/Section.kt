package com.cropo.world.dungeon

import com.cropo.world.dungeon.layout.LayoutElement
import com.cropo.world.dungeon.layout.LayoutElement.*
import com.cropo.world.dungeon.layout.SectionLayoutStrategy
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * A section of the map on defined position with a defined size,
 * containing and managing the layout of this section
 */
class Section(
    val bounds: Rect
) {
    private val internalLayout: MutableMap<Position, LayoutElement> = HashMap()

    /**
     * Each available [Position] mapped to the [LayoutElement] which describes the content
     */
    val layout: Map<Position, LayoutElement>
        get() = internalLayout

    init {
        bounds.fetchPositions().forEach {
            internalLayout[it] = WALL
        }
    }

    /**
     * Procedurally generate the layout for this [Section] section
     * with the given [SectionLayoutStrategy]
     */
    fun generateLayoutWith(strategy: SectionLayoutStrategy) {
        mergeData(strategy.generateTerrain(bounds))
    }

    /**
     * Merge another [Section] into this one
     */
    fun merge(section: Section) {
        mergeData(section.layout)
    }

    /**
     * Merges a data set into the layout
     *
     * Everything outside the bounds of this [Section] will not be merged
     */
    private fun mergeData(data: Map<Position, LayoutElement>) {
        data.filter { entry -> entry.value != WALL }
            .filter { entry -> bounds.containsPosition(entry.key) }
            .forEach { (position, element) ->
                internalLayout[position] = element
            }
    }

}