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

There is still a lot left to fix, the game is far from being able to run or compile, and the class which will be
the biggest challenge to fix is `WorldBlock` - there is a lot of interaction with the entities, which is all broken
right now.

In the first step of fixing this, I have to pass a reference of `EntityEngine` all the way down from `main.kt` over
`World` to the `WorldBlock`. After that, I will change all affected properties and methods one by one, 
and the first change needs to be done here:
```kotlin
private val exploredEmptyTile: Tile?
    get() = entities.firstOrNull { it.isExplored && it.type == EntityType.TERRAIN }?.tileExplored
```

I changed it to this:

```kotlin
private val exploredEmptyTile: Tile?
    get() =
        entities.filter {
            entityEngine.get(it, GridTile::class)!!.layer == TileLayer.TERRAIN
        }.map {
            entityEngine.get(it, GridTile::class)!!.tileHidden
        }.firstOrNull()
```
For all entities which are positioned on this block, I check if they are set to the `TERRAIN` layer, and then
I simply pick the `tileHidden` of the first available entity. 

I _really_ hope there are enough `null` checks here. I was told kotlin is very safe regarding this, 
but as a professional Java developer, the fear of a `NullPointerException` haunts me day and night.

Onto the next property which needs a change:

```kotlin
override val emptyTile: Tile
    get() =
        when {
            !isVisible -> exploredEmptyTile ?: super.emptyTile
            entities.any { it.type == EntityType.TERRAIN } -> entities.first { it.type == EntityType.TERRAIN }.tile
            else -> super.emptyTile
        }
```

I turned it to this:
```kotlin
override val emptyTile: Tile
    get() =
        when {
            !isVisible -> exploredEmptyTile ?: super.emptyTile
            terrainLayerEntities.isNotEmpty() -> terrainLayerEntities.map {
                entityEngine.get(it, GridTile::class)!!.tileVisible
            }.first()
            else -> super.emptyTile
        }
```

Because access to all entities on the `TERRAIN` layer is used three times in total, I made a property out of it, too:

```kotlin
private val terrainLayerEntities: List<UUID>
    get() = entities.filter {
        entityEngine.has(it, GridTile::class)
                && entityEngine.get(it, GridTile::class)!!.layer == TileLayer.TERRAIN
    }
``` 

Of course, I did change `exploredEmptyTile` to use `terrainLayerEntities`, too

And the next one: 

```kotlin
override var tiles: PersistentMap<BlockTileType, Tile>
    get() = persistentMapOf(
        Pair(
            BlockTileType.TOP, when {
                !isVisible -> emptyTile
                entities.isEmpty() -> emptyTile
                else -> entities.first().tile
            }
        )
    )
    set(value) {}
```

Not too much of a change, here. I really hope this works as expected, and `filter` doesn't mess up the sorted list.

```kotlin
override var tiles: PersistentMap<BlockTileType, Tile>
    get() = persistentMapOf(
        Pair(
            BlockTileType.TOP, when {
                !isVisible || entities.isEmpty() -> emptyTile
                else -> entities.filter {
                    entityEngine.has(it, GridTile::class)
                }.map {
                    entityEngine.get(it, GridTile::class)!!.tileVisible
                }.first()
            }
        )
    )
    set(value) {}
```

Two more methods are still missing a change - `addEntity` and `removeEntity`. Because the change is pretty small
this time, I will just post the changed methods

```kotlin
fun addEntity(entity: UUID) {
    if(!entityEngine.has(entity, GridTile::class)) {
        return
    }
    entities.add(entity)
    entities.sortBy {
        entityEngine.get(it, GridTile::class)?.layer
    }
}

fun removeEntity(entity: UUID) {
    entities.remove(entity)
}
```

The one thing I added is the initial check if an entity has a `GridTile` component.