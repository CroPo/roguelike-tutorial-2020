package com.cropo.entity

import com.cropo.entity.component.Component
import org.hexworks.cobalt.core.api.UUID
import kotlin.reflect.KClass

/**
 * Provides access to all entities and all of their assigned [Component]s
 */
class EntityEngine {
    private val componentStorage: MutableMap<KClass<out Component>, MutableMap<UUID, Component>> = mutableMapOf()

    /**
     * Generate a random [UUID] for a new entity
     */
    fun createEntity(): UUID {
        return UUID.randomUUID()
    }

    /**
     * Add a component and assign it to an entity. If a component of the same type is already assigned to the
     * entity, it will be replaced.
     */
    fun assignComponent(entityId: UUID, component: Component) {
        getOrCreateComponentMap(component)[entityId] = component
    }

    /**
     * Add a set of components and assign them to an entity. If a component of the same type is already assigned to the
     * entity, it will be replaced.
     */
    fun assignComponents(entityId: UUID, components: Set<Component>) {
        components.forEach {
            assignComponent(entityId, it)
        }
    }

    /**
     * Check if a [Component] is available for the entity
     */
    fun has(entityId: UUID, componentClass: KClass<out Component>): Boolean {
        return componentStorage[componentClass] != null && componentStorage[componentClass]?.containsKey(entityId)!!
    }

    /**
     * Get a specific [Component] of an entity. Returns null if the entity doesn't have the named [Component] assigned
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Component> get(entityId: UUID, componentClass: KClass<T>): T? {
        return componentStorage[componentClass]?.get(entityId) as T?
    }

    /**
     * Get a Map of all components of a specific type
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Component> get(componentClass: KClass<T>) : Map<UUID, T> {
        return componentStorage[componentClass] as Map<UUID, T>
    }

    /**
     * Return a map for a specific [Component] type. If it doesn't exist it will be created
     */
    private fun getOrCreateComponentMap(component: Component): MutableMap<UUID, Component> {
        return if (componentStorage[component::class] != null) {
            componentStorage[component::class]!!
        } else {
            val componentMap = mutableMapOf<UUID, Component>()
            componentStorage[component::class] = componentMap
            componentMap
        }
    }
}