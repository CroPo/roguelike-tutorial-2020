package com.cropo.entity.component

import org.hexworks.zircon.api.data.Position3D

data class Position(
    var position3D: Position3D = Position3D.defaultPosition()
) : Component