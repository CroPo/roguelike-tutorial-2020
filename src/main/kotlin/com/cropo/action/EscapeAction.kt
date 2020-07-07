package com.cropo.action

import com.cropo.engine.Engine
import kotlin.system.exitProcess

/**
 * Perform any action associated with pressing the `ESC` key
 */
class EscapeAction : Action {
    override fun perform(engine: Engine, entity: Entity) {
        exitProcess(0)
    }
}