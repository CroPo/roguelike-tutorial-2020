package com.cropo.entity.component

import org.hexworks.zircon.api.data.Tile

data class GridAttributes(
    val isBlocking: Boolean = true,
    val isTransparent: Boolean = true,
    var isExplored: Boolean = false
) : Component