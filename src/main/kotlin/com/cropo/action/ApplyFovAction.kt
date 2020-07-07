package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import com.cropo.world.World

/**
 * Update the FOV of an [Entity]
 */
class ApplyFovAction(val world: World) : Action {
    override fun perform(engine: Engine, entity: Entity) {
        if (entity.fieldOfVision == null) {
            return
        }
        world.updateFov(entity.fieldOfVision)

        engine.entities.filter {
            entity.fieldOfVision.contains(it.position) && it.type == EntityType.TERRAIN
        }.forEach {
            it.isExplored = true
        }
    }
}