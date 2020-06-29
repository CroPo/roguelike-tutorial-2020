package com.cropo

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.builder.component.GameComponentBuilder
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.*
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.grid.TileGrid

fun main(args: Array<String>) {
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

    gameArea.setBlockAt(
        Position3D.create(40,25,0),
        Block.create(Tile.createCharacterTile('@', StyleSet.defaultStyle()))
    )

    screen.display()

}