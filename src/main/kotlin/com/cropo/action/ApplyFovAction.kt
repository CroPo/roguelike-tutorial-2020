package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.component.FieldOfView
import com.cropo.entity.component.GridPosition
import com.cropo.entity.component.GridTile
import com.cropo.world.World
import org.hexworks.cobalt.core.api.UUID

/**
 * Update the FOV of an [Entity]
 */
class ApplyFovAction(val world: World) : Action {
    override fun perform(engine: Engine, entityId: UUID) {
        if (!engine.entityEngine.has(entityId, FieldOfView::class) ||
            !engine.entityEngine.has(entityId, GridPosition::class)
        ) {
            return
        }

        val visible = engine.entityEngine.get(entityId, FieldOfView::class)!!.visible

        world.updateFov(visible)

        visible.forEach { position ->
            val entitiesOnPosition = engine.gameArea.fetchBlockAt(position).get().getEntityList()
            entitiesOnPosition.mapNotNull { entityId ->
                engine.entityEngine.get(entityId, GridTile::class)
            }.forEach { gridAttributes ->
                gridAttributes.isExplored = true
            }
        }
    }
}