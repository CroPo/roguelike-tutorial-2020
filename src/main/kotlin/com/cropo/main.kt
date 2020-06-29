package com.cropo

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.entity.Entity
import com.cropo.input.handleKeyboardEvent
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.GameComponentBuilder
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.*
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Pass
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val player = Entity(Position3D.create(40, 25, 0),
    '@', TileColor.defaultForegroundColor())
    val npc = Entity(Position3D.create(20, 12, 0),
        '@', TileColor.defaultForegroundColor())
    val entities = listOf(player, npc)

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
                player.position = player.position.withRelativeX(action.dx).withRelativeY(action.dy)
            }
        }

        for (x in 0..80) {
            for (y in 0..50) {
                gameArea.setBlockAt(Position3D.create(x, y, 0), Block.create(Tile.defaultTile()))
            }
        }

        for (entity in entities) {
            gameArea.setBlockAt(
                entity.position,
                Block.create(Tile.newBuilder()
                    .withCharacter(entity.character)
                    .withForegroundColor(entity.color)
                    .build())
            )
        }
        Pass
    }

    for (entity in entities) {
        gameArea.setBlockAt(
            entity.position,
            Block.create(Tile.newBuilder()
                .withCharacter(entity.character)
                .withForegroundColor(entity.color)
                .build())
        )
    }

    screen.display()
}