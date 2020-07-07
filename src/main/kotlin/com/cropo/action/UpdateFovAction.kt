package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.EllipseParameters
import org.hexworks.zircon.api.shape.LineFactory

/**
 * Update the FOV of an [Entity]
 */
class UpdateFovAction : Action {
    override fun perform(engine: Engine, entity: Entity) {
        if (entity.fieldOfVision == null) {
            return
        }

        val center = entity.position.to2DPosition()

        val fovCircle = EllipseFactory.buildEllipse(
            EllipseParameters(center, Size.create(11, 11))
        )

        val visiblePositions: MutableList<Position3D> = mutableListOf()
        fovCircle.positions.forEach { fovPosition ->
            LineFactory.buildLine(center, fovPosition).positions
                .filterNot { visiblePositions.contains(it.to3DPosition(0)) }
                .takeWhile { linePosition ->
                    !engine.entities.filter { it.position.to2DPosition() == linePosition }
                        .any { !it.isTransparent }
                }
                .forEach { visiblePositions.add(it.to3DPosition(0)) }
        }

        entity.fieldOfVision.clear()
        entity.fieldOfVision.addAll(visiblePositions)
    }
}