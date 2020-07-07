package com.cropo.engine

import com.cropo.action.ApplyFovAction
import com.cropo.action.UpdateFovAction
import com.cropo.entity.Entity
import com.cropo.input.handleKeyboardEvent
import com.cropo.world.World
import org.hexworks.zircon.api.uievent.KeyboardEvent

class Engine(
    val gameArea: World,
    val entities: List<Entity>,
    private val player: Entity
) {

    fun handleEvents(event: KeyboardEvent) {
        handleKeyboardEvent(event).perform(this, player)
        UpdateFovAction().perform(this, player)
        ApplyFovAction(gameArea).perform(this, player)
    }

    fun render() {
        entities.forEach {
            gameArea.fetchBlockAt(it.position).get().addEntity(it)
        }
        UpdateFovAction().perform(this, player)
        ApplyFovAction(gameArea).perform(this, player)
    }
}