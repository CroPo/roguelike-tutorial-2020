package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import kotlin.system.exitProcess

/**
 * Update the FOV of an [Entity]
 */
class UpdateFovAction : Action {
    override fun perform(engine: Engine, entity: Entity) {
        if (entity.fieldOfVision == null) {
            return
        }
    }
}