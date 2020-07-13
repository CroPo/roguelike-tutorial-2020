package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.component.Actor
import com.cropo.entity.component.GridPosition
import com.cropo.entity.component.GridTile
import com.cropo.entity.component.Terrain
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
            engine.gameArea.fetchBlockAt(targetPosition).get().getEntityList().filter {
                engine.entityEngine.has(it, GridTile::class) &&
                        engine.entityEngine.get(it, GridTile::class)!!.isBlocking
            }

        when {
            entitiesOnTarget.any {
                engine.entityEngine.has(it, Actor::class)
            } -> println("Kicked someone. They don't really mind that")
            entitiesOnTarget.any {
                engine.entityEngine.has(it, Terrain::class)
            } -> println("Bumped into a wall. BONK!")
            else -> {
                engine.gameArea.fetchBlockAt(entityPosition.position3D).get().removeEntity(entityId)
                engine.gameArea.fetchBlockAt(targetPosition).get().addEntity(entityId)
                entityPosition.position3D = targetPosition
            }
        }
    }
}