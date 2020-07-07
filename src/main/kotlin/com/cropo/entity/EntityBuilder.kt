package com.cropo.entity

import com.cropo.entity.component.Component
import org.hexworks.cobalt.core.api.UUID

/**
 * A builder for entities
 */
class EntityBuilder(private val entityEngine: EntityEngine) {

    private val components = mutableSetOf<Component>()

    fun with(component: Component) = also {
        components.add(component)
    }

    fun with(components: Collection<Component>) = also {
        components.forEach { with(it) }
    }

    fun build(): UUID {
        val entity = entityEngine.createEntity()
        entityEngine.assignComponents(entity, components)
        return entity
    }

    companion object {
        fun createBuilder(entityEngine: EntityEngine): EntityBuilder {
            return EntityBuilder(entityEngine)
        }
    }
}