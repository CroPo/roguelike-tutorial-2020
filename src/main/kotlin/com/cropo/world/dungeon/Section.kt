package com.cropo.world.dungeon

import com.cropo.entity.EntityEngine
import com.cropo.world.dungeon.layout.LayoutElement
import com.cropo.world.dungeon.layout.LayoutElement.*
import com.cropo.world.dungeon.layout.SectionLayoutStrategy
import com.cropo.world.dungeon.spawn.SpawnStrategy
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import kotlin.random.Random

/**
 * A section of the map on defined position with a defined size,
 * containing and managing the layout of this section
 */
class Section(
    val bounds: Rect,
    private val rng: Random,
    private val entityEngine: EntityEngine
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
     * Procedurally generate the layout for this [Section]
     */
    fun generateLayoutWith(strategy: SectionLayoutStrategy) = also {
        mergeLayout(strategy.generateTerrain(bounds, rng))
    }

    /**
     * Spawn a number of entities inside this [Section] using a [SpawnStrategy]
     */
    fun spawnWith(strategy: SpawnStrategy) = also {
        strategy.spawn(rng, entityEngine, layout)
    }

    /**
     * Merge the layout of this section into another one
     */
    fun mergeInto(targetSection: Section) = also {
        targetSection.merge(this)
    }

    /**
     * Merge another [Section] into this one
     */
    fun merge(section: Section) {
        mergeLayout(section.layout)
    }

    /**
     * Merges a data set into the layout
     *
     * Everything outside the bounds of this [Section] will not be merged
     */
    private fun mergeLayout(layout: Map<Position, LayoutElement>) {
        layout.filter { (position, _) -> bounds.containsPosition(position) }
            .forEach { (position, element) ->
                if (internalLayout[position] != FLOOR) {
                    internalLayout[position] = element
                }
            }
    }
}