package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.component.FieldOfView
import com.cropo.entity.component.GridAttributes
import com.cropo.entity.component.GridPosition
import com.cropo.world.World
import org.hexworks.cobalt.core.api.UUID
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

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

        val entityPosition = engine.entityEngine.get(entityId, GridPosition::class)!!.position3D
        val visible = engine.entityEngine.get(entityId, FieldOfView::class)!!.visible

        world.updateFov(visible)

        visible.forEach {
            engine.entityEngine.get(GridAttributes::class).filter { (entityId, _) ->
                engine.entityEngine.has(entityId, GridPosition::class) &&
                        engine.entityEngine.get(entityId, GridPosition::class)!!.position3D == it
            }.forEach { (_, gridAttributes) ->
                gridAttributes.isExplored = true
            }
        }
    }
}