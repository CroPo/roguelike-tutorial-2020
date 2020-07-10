package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.component.FieldOfView
import com.cropo.entity.component.GridAttributes
import com.cropo.entity.component.GridPosition
import org.hexworks.cobalt.core.api.UUID

/**
 * Move an entity to a specified position
 */
class MovementAction(val dx: Int = 0, val dy: Int = 0) : Action {
    override fun perform(engine: Engine, entityId: UUID) {
        if (!engine.entityEngine.has(entityId, GridPosition::class)) {
            return
        }

        val entityPosition = engine.entityEngine.get(entityId, GridPosition::class)
        val targetPosition = entityPosition!!.position3D.withRelativeX(dx).withRelativeY(dy)
        val entitiesOnTarget =
            engine.gameArea.fetchBlockAt(targetPosition).get().getEntityList()

        when {
            targetPosition.x !in 0 until engine.gameArea.actualSize.xLength
                    || targetPosition.y !in 0 until engine.gameArea.actualSize.yLength
            -> println(
                "Can't move beyond the edge of the world"
            )
            entitiesOnTarget.mapNotNull {
                engine.entityEngine.get(it, GridAttributes::class)
            }.any {
                it.isBlocking
            } -> println("Walked into a wall. BONK!")
            else -> {
                engine.gameArea.fetchBlockAt(entityPosition.position3D).get().removeEntity(entityId)
                engine.gameArea.fetchBlockAt(targetPosition).get().addEntity(entityId)
                entityPosition.position3D = targetPosition
            }
        }
    }
}