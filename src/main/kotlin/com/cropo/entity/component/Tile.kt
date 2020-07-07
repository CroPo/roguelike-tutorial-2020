package com.cropo.entity.component

import com.cropo.tile.TileLayer
import org.hexworks.zircon.api.data.Tile

data class Tile(
    val tileVisible: Tile,
    val tileHidden: Tile? = null,
    val layer: TileLayer = TileLayer.TERRAIN
) : Component