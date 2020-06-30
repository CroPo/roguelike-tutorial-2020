package com.cropo

import com.cropo.action.EscapeAction
import com.cropo.action.MovementAction
import com.cropo.engine.Engine
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
import org.hexworks.zircon.api.uievent.Processed
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val player = Entity(
        Position3D.create(40, 25, 0),
        '@', TileColor.defaultForegroundColor()
    )
    val npc = Entity(
        Position3D.create(20, 12, 0),
        '@', TileColor.defaultForegroundColor()
    )
    val entities = listOf(player, npc)
    val engine = Engine(entities, player)

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

    engine.render(gameArea)

    screen.addComponent(
        GameComponentBuilder.newBuilder<Tile, Block<Tile>>()
            .withGameArea(gameArea)
            .build()
    )

    screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        engine.handleEvents(event)
        engine.render(gameArea)
        Processed
    }

    screen.display()
}