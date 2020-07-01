package com.cropo

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import com.cropo.entity.EntityBlueprint
import com.cropo.entity.EntityType
import com.cropo.tile.TileBlueprint
import com.cropo.world.WorldBlock
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
import org.hexworks.zircon.api.uievent.Processed

fun main(args: Array<String>) {

    val screenSize = Size.create(80, 50)
    val mapSize = Size3D.create(80, 50, 1)

    val player = Entity(
        Position3D.create(screenSize.width / 2, screenSize.height / 2, 0),
        EntityType.ACTOR,
        Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.defaultForegroundColor())
            .build()
    )
    val entities = listOf(
        player,
        EntityBlueprint.wallEntity(Position3D.create(15, 15, 0)),
        EntityBlueprint.floorEntity(Position3D.create(15, 16, 0))
    )

    val grid: TileGrid = SwingApplications.startTileGrid(
        AppConfig.newBuilder()
            .withTitle("/r/roguelikedev tutorial 2020")
            .withSize(screenSize.width, screenSize.height)
            .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
            .build()
    )

    val screen = ScreenBuilder.createScreenFor(grid)

    val gameArea = GameAreaBuilder.newBuilder<Tile, WorldBlock>()
        .withVisibleSize(mapSize)
        .withActualSize(mapSize)
        .build()

    // temporary - create all blocks in game area
    for(x in 0..screenSize.width) {
        for(y in 0..screenSize.height) {
            gameArea.setBlockAt(Position3D.create(x,y,0),WorldBlock())
        }
    }

    val engine = Engine(gameArea, entities, player)
    engine.render()

    screen.addComponent(
        GameComponentBuilder.newBuilder<Tile, WorldBlock>()
            .withGameArea(gameArea)
            .build()
    )

    screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        engine.handleEvents(event)
        Processed
    }

    screen.display()
}