package com.cropo

import com.cropo.engine.Engine
import com.cropo.entity.EntityBuilder
import com.cropo.entity.EntityEngine
import com.cropo.entity.component.*
import com.cropo.tile.TileBlueprint
import com.cropo.tile.TileLayer
import com.cropo.world.dungeon.DungeonGenerator
import com.cropo.world.World
import com.cropo.world.WorldBlock

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.GameComponentBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.*
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed

fun main(args: Array<String>) {

    val screenSize = Size.create(80, 50)
    val mapSize = Size3D.create(80, 50, 1)

    val entityEngine = EntityEngine()

    val player = EntityBuilder.createBuilder(entityEngine).with(
        setOf(
            Actor(),
            GridPosition(),
            Name(
                name = "Player",
                description = "This is you"
            ),
            GridAttributes(
                isBlocking = true,
                isTransparent = true
            ),
            GridTile(
                tileVisible = TileBlueprint.player()
            ),
            FieldOfView()
        )
    ).build()

    val grid: TileGrid = SwingApplications.startTileGrid(
        AppConfig.newBuilder()
            .withTitle("/r/roguelikedev tutorial 2020")
            .withSize(screenSize.width, screenSize.height)
            .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
            .build()
    )

    val screen = ScreenBuilder.createScreenFor(grid)

    val world = World(mapSize, mapSize, entityEngine)

    val dungeonGenerator = DungeonGenerator(mapSize.to2DSize())
    dungeonGenerator.generateLevel(entityEngine, player)

    val engine = Engine(world, entityEngine, player)
    engine.render()

    screen.addComponent(
        GameComponentBuilder.newBuilder<Tile, WorldBlock>()
            .withGameArea(world)
            .build()
    )

    screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        engine.handleEvents(event)
        Processed
    }

    screen.display()
}