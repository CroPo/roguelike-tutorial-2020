package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity

/**
 * Move an entity to a specified position
 */
class MovementAction(val dx: Int = 0, val dy: Int = 0) : Action {
    override fun perform(engine: Engine, entity: Entity) {
        val targetPosition  = entity.position.withRelativeX(dx).withRelativeY(dy)

        when {
            targetPosition.x !in 0 until engine.gameArea.actualSize.xLength || targetPosition.y !in 0 until engine.gameArea.actualSize.yLength -> println("Can't move beyond the edge of the world")
            engine.entities.filter { it.position == targetPosition }.any { !it.walkable } -> println("Walked into a wall. BONK!")
            else -> entity.position = targetPosition
        }
    }
}