package com.cropo.entity.component

import org.hexworks.zircon.api.data.Tile

data class GridTile(
    val tileVisible: Tile,
    val tileHidden: Tile? = null,
    val isBlocking: Boolean = false,
    val isTransparent: Boolean = false,
    var isExplored: Boolean = false
) : Component