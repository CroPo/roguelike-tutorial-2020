package com.cropo.engine

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.entity.Entity
import com.cropo.input.handleKeyboardEvent
import com.cropo.world.WorldBlock
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.uievent.KeyboardEvent
import kotlin.system.exitProcess

class Engine(private val entities: List<Entity>, private val player: Entity) {

    fun handleEvents(event: KeyboardEvent) {
        when (val action = handleKeyboardEvent(event)) {
            is EscapeAction -> exitProcess(0)
            is MovementAction -> {
                player.position = player.position.withRelativeX(action.dx).withRelativeY(action.dy)
            }
        }
    }

    fun render(gameArea: GameArea<Tile, WorldBlock>) {
        for (entity in entities) {
            gameArea.fetchBlockAt(entity.position).get().addEntity(entity)
        }
    }
}