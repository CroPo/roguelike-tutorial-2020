package com.cropo.entity.component

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D

class GridPosition(
    var position3D: Position3D = Position3D.defaultPosition()
) : Component {
    var position2D : Position
        get() = position3D.to2DPosition()
        set(value) {
            position3D = value.toPosition3D(position3D.z)
        }
}