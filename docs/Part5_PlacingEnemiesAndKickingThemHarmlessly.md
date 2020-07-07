# Part 5 - Placing Enemies and kicking them (harmlessly)

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/hif9df/roguelikedev_does_the_complete_roguelike_tutorial/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/v2/part-5/)

## Taming the first monster

Before I continue with the tutorial, I will address a few problems which came up during the development. One is,
of course, the structure of the whole project, which is more spaghetti code than anything else so far. 

The input handler is just something function in a random place. Entities are getting more and more bloated. I don't
have a real event system. There is no one place which holds the game's information, and many more.

I can't change all of that at once, so I will start with the one which needs treatment most of all - the `Entity`. Right
now, the class already has 7 attributes, and the number will just be increasing in the future, so I will have to
split that up a bit. Also, again, I won't be adding an actual ECS here.

First, I will wrap every attribute an entity has in a `Component`, so that in the end the `Entity` isn't much
more than a `List<Component>`

I created a component for every (somewhat) logical group of attributes. I renamed a few things, so they will fit
better into the new structure. Also, the `EntityType` has been moved to  `tile.TileLayer`. The values are still
the same.
```kotlin
interface Component

data class Position(
    var position3D: Position3D = Position3D.defaultPosition()
) : Component

data class Tile(
    val tileVisible: Tile,
    val tileHidden: Tile
) : Component

data class MapAttributes(
    val isBlocking: Boolean = true,
    val isTransparent: Boolean = true,
    var isExplored: Boolean = false
) : Component

data class FieldOfView(
    val fieldOfVision: MutableList<Position3D> = mutableListOf()
) : Component
```

The `Component` interface is empty on intention - I just need it for grouping the components together. Further, as promised,
the `Entity` class has been reduced to a single list.

```kotlin
class Entity(
    val components : MutableList<Component>
)
```

At this point, I don't even dare to hit the _run_ button. 

The next step, and maybe the easiest, ist getting the `EntityBlueprint` object updated. This is how the 
`wall` blueprint looks now - the `floor` is very similar to this. And the `player` entity created in `main.kt` is
updated, too.

```kotlin
fun wallEntity(position: Position3D): Entity {
    return Entity(
        mutableListOf(
            Position(position),
            Tile(
                tileVisible = TileBlueprint.wall(),
                tileHidden = TileBlueprint.wallExplored()
            ),
            MapAttributes(
                isBlocking = true
            )
        )
    )
}
```

Before I can continue further, I need to implement a few more things. Right now, I have no way to access a specific
`Component` of an entity (or even check for its existence) in a somewhat convenient way. I need something which handles all
access and management of the entities.

With all of what I have created here before in mind, I can actually reduce the `Entity` itself to an `id`. This is
the _only_ thing all entities will have in common, maybe not now, but very soon. In my case, the `id` of an entity
will be a randomly generated `UUID` (which is also provided by zircon).

For a start, I created the class `EntityEngine` here, which should fulfil all the mentioned requirements.

```kotlin
class EntityEngine {
    private val componentStorage : MutableMap<KClass<Component>, MutableMap<UUID, Component>> = mutableMapOf()
}
```

All componets in the storage are mapped to their entity `UUID`, and all those mapped components of the same type 
are mapped to their corresponding type.

```kotlin
fun createEntity(): UUID {
    return UUID.randomUUID()
}

fun addComponent(entityId: UUID, component: Component) {
    getOrCreateComponentMap(component)[entityId] = component
}

fun addComponents(entityId: UUID, components: Set<Component>) {
    components.forEach {
        addComponent(entityId, it)
    }
}

private fun getOrCreateComponentMap(component: Component): MutableMap<UUID, Component> {
    return if (componentStorage[component::class] != null) {
        componentStorage[component::class]!!
    } else {
        val componentMap = mutableMapOf<UUID, Component>()
        componentStorage[component::class] = componentMap
        componentMap
    }
}
```

This is the first bunch of methods. On for creating a new entity (a random `UUID` here), and two for assigning
components to the entities. For easier access, I added a `EntityBuilder` class, and once again updated the 
`EntityBlueprint` object.

Next up, I will implement accessors to the components.
```kotlin
fun has(entityId: UUID, componentClass: KClass<out Component>): Boolean {
    return componentStorage[componentClass] != null && componentStorage[componentClass]?.containsKey(entityId)!!
}

@Suppress("UNCHECKED_CAST")
fun <T : Component> get(entityId: UUID, componentClass: KClass<T>) : T? {
    return componentStorage[componentClass]?.get(entityId) as T?
}
```
With these, I can access specific components of a single entity. I decided to keep the names short - since the only
_thing_ which could be queried are components anyways. There is no check planned if an entity _exists_, because
an entity without any components is useless.

To get all entities of a specific type, I added this method:
```kotlin
@Suppress("UNCHECKED_CAST")
fun <T : Component> get(componentClass: KClass<T>) : Map<UUID, T> {
    return componentStorage[componentClass] as Map<UUID, T>
}
```

There is still a lot left to fix, the game is far from being able to run or compile. 