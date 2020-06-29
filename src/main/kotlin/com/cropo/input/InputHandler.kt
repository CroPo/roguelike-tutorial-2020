package com.cropo.input

import com.cropo.action.Action
import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.action.NoAction
import org.hexworks.zircon.api.uievent.*

fun handleKeyboardEvent(event: KeyboardEvent) : Action {
    return when (event.code) {
        KeyCode.UP -> MovementAction(dy = -1)
        KeyCode.DOWN -> MovementAction(dy = 1)
        KeyCode.LEFT -> MovementAction(dx = -1)
        KeyCode.RIGHT -> MovementAction(dx = 1)
        KeyCode.ESCAPE -> EscapeAction()
        else -> {
            NoAction()
        }
    }
}

