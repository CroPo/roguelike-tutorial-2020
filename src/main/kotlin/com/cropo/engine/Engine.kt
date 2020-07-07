package com.cropo.engine

import com.cropo.action.ApplyFovAction
import com.cropo.action.UpdateFovAction
import com.cropo.entity.EntityEngine
import com.cropo.entity.component.GridTile
import com.cropo.entity.component.GridPosition
import com.cropo.input.handleKeyboardEvent
import com.cropo.world.World
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.uievent.KeyboardEvent

class Engine(
    val gameArea: World,
    val entityEngine: EntityEngine,
    private val player: UUID
) {

    fun handleEvents(event: KeyboardEvent) {
        handleKeyboardEvent(event).perform(this, player)
        UpdateFovAction().perform(this, player)
        ApplyFovAction(gameArea).perform(this, player)
    }

    fun render() {
        entityEngine.get(GridPosition::class).filter {
            (entityId, _) ->
            entityEngine.has(entityId, GridTile::class)
        }.forEach {
            (entityId, position) ->
            gameArea.fetchBlockAt(position.position3D).get().addEntity(entityId)
        }
        UpdateFovAction().perform(this, player)
        ApplyFovAction(gameArea).perform(this, player)
    }
}