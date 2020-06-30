package com.cropo.entity

import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile

class Entity(
    var position: Position3D,
    val type: EntityType,
    val tile: Tile
)