package com.cropo.engine

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.entity.Entity
import com.cropo.input.handleKeyboardEvent
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

    fun render(gameArea: GameArea<Tile, Block<Tile>>) {

        for (x in 0..gameArea.actualSize.xLength) {
            for (y in 0..gameArea.actualSize.yLength) {
                gameArea.setBlockAt(Position3D.create(x, y, 0), Block.create(Tile.defaultTile()))
            }
        }

        for (entity in entities) {
            gameArea.setBlockAt(
                entity.position,
                Block.create(
                    Tile.newBuilder()
                        .withCharacter(entity.character)
                        .withForegroundColor(entity.color)
                        .build()
                )
            )
        }
    }
}