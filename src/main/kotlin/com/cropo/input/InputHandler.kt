package com.cropo.input

import com.cropo.action.Action
import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.action.NoAction
import org.hexworks.zircon.api.uievent.*

fun handleKeyboardEvent(event: KeyboardEvent): Action {
    return when (event.code) {
        KeyCode.UP -> MovementAction(dy = -1)
        KeyCode.KP_UP -> MovementAction(dy = -1)
        KeyCode.DOWN -> MovementAction(dy = 1)
        KeyCode.KP_DOWN -> MovementAction(dy = 1)
        KeyCode.LEFT -> MovementAction(dx = -1)
        KeyCode.KP_LEFT -> MovementAction(dx = -1)
        KeyCode.RIGHT -> MovementAction(dx = 1)
        KeyCode.KP_RIGHT -> MovementAction(dx = 1)
        KeyCode.HOME -> MovementAction(dx = -1, dy = -1)
        KeyCode.PAGE_UP -> MovementAction(dx = 1, dy = -1)
        KeyCode.END -> MovementAction(dx = -1, dy = 1)
        KeyCode.PAGE_DOWN -> MovementAction(dx = 1, dy = 1)
        KeyCode.ESCAPE -> EscapeAction()
        else -> {
            NoAction()
        }
    }
}

