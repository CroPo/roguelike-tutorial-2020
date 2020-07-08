package com.cropo.action

import com.cropo.engine.Engine
import org.hexworks.cobalt.core.api.UUID

interface Action {
    fun perform(engine: Engine, entityId: UUID)
}