package com.cropo.world

import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.base.BaseGameArea

class World(
    actualSize: Size3D,
    visibleSize: Size3D
) : BaseGameArea<Tile, WorldBlock>(actualSize, visibleSize) {
    init {
        actualSize.fetchPositions().forEach { position ->
            setBlockAt(position, WorldBlock())
        }
    }
}