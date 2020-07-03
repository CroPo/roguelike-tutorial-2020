package com.cropo.world.dungeon

import com.cropo.world.dungeon.LayoutElement.*
import com.cropo.world.dungeon.layout.NoLayout
import com.cropo.world.dungeon.layout.SectionLayoutStrategy
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/**
 * A section of the map on defined position with a defined size,
 * containing and managing the layout of this section
 */
class Section(
    val bounds: Rect,
    private val layoutStrategy: SectionLayoutStrategy = NoLayout()
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
        mergeData(layoutStrategy.generateTerrain(bounds))
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

    /**
     * Merge another section into this section
     */
    fun merge(section: Section) {
        mergeData(section.layout)
    }

}