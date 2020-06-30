package com.cropo.engine

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.entity.Entity
import com.cropo.input.handleKeyboardEvent
import com.cropo.world.WorldBlock
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.uievent.KeyboardEvent
import kotlin.system.exitProcess

class Engine(
    private val gameArea: GameArea<Tile, WorldBlock>,
    private val entities: List<Entity>,
    private val player: Entity
) {

    fun handleEvents(event: KeyboardEvent) {
        when (val action = handleKeyboardEvent(event)) {
            is EscapeAction -> exitProcess(0)
            is MovementAction -> {
                val targetPosition  = player.position.withRelativeX(action.dx).withRelativeY(action.dy)

                when {
                    targetPosition.x in 0 until gameArea.actualSize.xLength && targetPosition.y in 0 until gameArea.actualSize.yLength -> println("Can't move beyond the edge of the world")
                    entities.filter { it.position == targetPosition }.any { !it.walkable } -> println("Moved into a wall. BONK!")
                    else -> player.position = targetPosition
                }
            }
        }
    }

    fun render() {
        for (entity in entities) {
            gameArea.fetchBlockAt(entity.position).get().addEntity(entity)
        }
    }
}