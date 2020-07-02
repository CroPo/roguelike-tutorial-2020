package com.cropo.engine

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.entity.Entity
import com.cropo.entity.EntityType
import com.cropo.input.handleKeyboardEvent
import com.cropo.world.WorldBlock
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.uievent.KeyboardEvent
import kotlin.system.exitProcess

class Engine(
    val gameArea: GameArea<Tile, WorldBlock>,
    val entities: List<Entity>,
    private val player: Entity
) {

    fun handleEvents(event: KeyboardEvent) {
        handleKeyboardEvent(event).perform(this, player)
    }

    fun render() {
        entities.filter { it.type == EntityType.ACTOR }.forEach {
            gameArea.fetchBlockAt(it.position).get().addEntity(it)
        }
    }
}