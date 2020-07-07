package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity
import com.cropo.entity.EntityType
import com.cropo.world.World
import com.cropo.world.WorldBlock
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.EllipseParameters
import org.hexworks.zircon.api.shape.LineFactory

/**
 * Update the FOV of an [Entity]
 */
class ApplyFovAction(val world: World) : Action {
    override fun perform(engine: Engine, entity: Entity) {
        if (entity.fieldOfVision == null) {
            return
        }
        world.updateFov(entity.fieldOfVision)

        engine.entities.filter {
            entity.fieldOfVision.contains(it.position) && it.type == EntityType.TERRAIN
        }.forEach {
            it.isExplored = true
        }
    }
}