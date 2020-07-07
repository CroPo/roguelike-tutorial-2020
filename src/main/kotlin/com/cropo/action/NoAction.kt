package com.cropo.action

import com.cropo.engine.Engine
import org.hexworks.cobalt.core.api.UUID

/**
 * Do nothing
 */
class NoAction : Action {
    override fun perform(engine: Engine, entity: UUID) {
    }
}