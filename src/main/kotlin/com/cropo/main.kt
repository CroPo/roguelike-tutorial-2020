package com.cropo

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.input.handleKeyboardEvent
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.GameComponentBuilder
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.*
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Pass
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    var playerPosition = Position3D.create(40, 25, 0)

    val grid: TileGrid = SwingApplications.startTileGrid(
        AppConfig.newBuilder()
            .withTitle("/r/roguelikedev tutorial 2020")
            .withSize(80, 50)
            .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
            .build()
    )

    val screen = ScreenBuilder.createScreenFor(grid)

    val gameArea = GameAreaBuilder.newBuilder<Tile, Block<Tile>>()
        .withVisibleSize(Size3D.create(80, 50, 1))
        .withActualSize(Size3D.create(80, 50, 1))
        .build()

    screen.addComponent(
        GameComponentBuilder.newBuilder<Tile, Block<Tile>>()
            .withGameArea(gameArea)
            .build()
    )

    screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        when (val action = handleKeyboardEvent(event)) {
            is EscapeAction -> exitProcess(0)
            is MovementAction -> {
                playerPosition = playerPosition.withRelativeX(action.dx).withRelativeY(action.dy)
            }
        }

        for (x in 0..80) {
            for (y in 0..50) {
                gameArea.setBlockAt(Position3D.create(x, y, 0), Block.create(Tile.defaultTile()))
            }
        }

        gameArea.setBlockAt(
            playerPosition,
            Block.create(Tile.createCharacterTile('@', StyleSet.defaultStyle()))
        )

        Pass
    }

    gameArea.setBlockAt(
        playerPosition,
        Block.create(Tile.createCharacterTile('@', StyleSet.defaultStyle()))
    )

    screen.display()
}