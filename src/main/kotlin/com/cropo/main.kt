package com.cropo

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import com.cropo.entity.EntityType
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

    val screenSize = Size.create(80, 50);

    val player = Entity(
        Position3D.create(screenSize.width / 2, screenSize.height / 2, 0),
        EntityType.ACTOR,
        Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.defaultForegroundColor())
            .build()
    )

    val npc = Entity(
        Position3D.create(screenSize.width / 3, screenSize.height / 3, 0),
        EntityType.ACTOR,
        Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.create(200, 200, 0))
            .build()
    )
    val entities = listOf(player, npc)
    val engine = Engine(entities, player)

    val grid: TileGrid = SwingApplications.startTileGrid(
        AppConfig.newBuilder()
            .withTitle("/r/roguelikedev tutorial 2020")
            .withSize(screenSize.width, screenSize.height)
            .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
            .build()
    )

    val screen = ScreenBuilder.createScreenFor(grid)

    val gameArea = GameAreaBuilder.newBuilder<Tile, Block<Tile>>()
        .withVisibleSize(Size3D.create(screenSize.width, screenSize.height, 1))
        .withActualSize(Size3D.create(screenSize.width, screenSize.height, 1))
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