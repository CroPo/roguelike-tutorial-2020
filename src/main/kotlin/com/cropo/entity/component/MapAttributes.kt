package com.cropo.entity.component

import org.hexworks.zircon.api.data.Tile

data class MapAttributes(
    val isBlocking: Boolean = true,
    val isTransparent: Boolean = true,
    var isExplored: Boolean = false
) : Component