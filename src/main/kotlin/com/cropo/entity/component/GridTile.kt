package com.cropo.entity.component

import com.cropo.tile.TileLayer
import org.hexworks.zircon.api.data.Tile

data class GridTile(
    val tileVisible: Tile,
    val tileHidden: Tile? = null,
    val layer: TileLayer = TileLayer.TERRAIN
) : Component