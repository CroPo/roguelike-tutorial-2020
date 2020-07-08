package com.cropo.action

import com.cropo.engine.Engine
import org.hexworks.cobalt.core.api.UUID
import kotlin.system.exitProcess

/**
 * Perform any action associated with pressing the `ESC` key
 */
class EscapeAction : Action {
    override fun perform(engine: Engine, entityId: UUID) {
        exitProcess(0)
    }
}