package com.cropo.action

import com.cropo.engine.Engine
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.shape.EllipseFactory
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
        val radius = 11

        val visiblePositions: MutableList<Position3D> = mutableListOf()

        EllipseFactory.buildEllipse(
            fromPosition = center,
            toPosition = center.withRelative(Position.create(radius, radius))
        ).positions.plus(
            EllipseFactory.buildEllipse(
                fromPosition = center,
                toPosition = center.withRelative(Position.create(radius - 1, radius - 1))
            ).positions
        ).forEach { fovPosition ->
            for (linePosition in LineFactory.buildLine(center, fovPosition).positions) {
                visiblePositions.add(linePosition.to3DPosition(0))
                if (engine.entities.filter { it.position.to2DPosition() == linePosition }
                        .any { !it.isTransparent }) {
                    break
                }
            }
        }


        entity.fieldOfVision.clear()
        entity.fieldOfVision.addAll(visiblePositions)
    }
}