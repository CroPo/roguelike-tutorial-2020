package com.cropo.entity.component

import org.hexworks.zircon.api.data.Position3D

data class FieldOfView(
    val fieldOfVision: MutableList<Position3D> = mutableListOf()
) : Component