package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity

/**
 * Do nothing
 */
class NoAction : Action {
    override fun perform(engine: Engine, entity: Entity) {
    }
}