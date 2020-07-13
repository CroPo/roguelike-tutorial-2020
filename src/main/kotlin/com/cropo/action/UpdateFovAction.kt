package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.component.FieldOfView
import com.cropo.entity.component.GridPosition
import com.cropo.entity.component.GridTile
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.LineFactory

/**
 * Update the FOV of an [Entity]
 */
class UpdateFovAction : Action {
    override fun perform(engine: Engine, entityId: UUID) {
        if (!engine.entityEngine.has(entityId, FieldOfView::class) ||
            !engine.entityEngine.has(entityId, GridPosition::class)
        ) {
            return
        }

        val fovComponent = engine.entityEngine.get(entityId, FieldOfView::class)!!
        val center = engine.entityEngine.get(entityId, GridPosition::class)!!.position2D
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

                val entitiesOnPosition =
                    engine.gameArea.fetchBlockAt(linePosition.to3DPosition(0)).get().getEntityList()

                if (entitiesOnPosition.mapNotNull {
                        engine.entityEngine.get(it, GridTile::class)
                    }.any {
                        !it.isTransparent
                    }) {
                    break
                }
            }
        }

        fovComponent.visible.clear()
        fovComponent.visible.addAll(visiblePositions)
    }
}