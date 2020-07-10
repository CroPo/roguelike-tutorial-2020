package com.cropo.entity.component

data class GridAttributes(
    val isBlocking: Boolean = false,
    val isTransparent: Boolean = false,
    var isExplored: Boolean = false
) : Component