# Part 2 - The generic Entity, the render functions, and the map

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/he3lfo/roguelikedev_does_the_complete_roguelike_tutorial/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/v2/part-2/)

## A generic entity

> Now that we can move our little ‘@’ symbol around, we need to give it something to move around in.
> But before that, let’s stop for a moment and think about the player object itself.

I like this quote from the tutorial. Right now, all I have is a single 3d coordinate, which represents
the position of _something_. And `Entity` is a good name for that thing. A set of values belonging to one
specific thing. With the scope of this tutorial, there is no need to use an _actual_ ECS, a set of
relatively static entities with a predefined set of components will be just fine enough.

I will probably expand this a bit further, we'll see. For now, a simple plain `Entity` class will do.

```kotlin
class Entity(var position: Position3D, val character: Char, val color : TileColor)
```

Yes, I absolutely love the short and pragmatic syntax of Kotlin.

After the change, the rendering looks a bit different, too:

```kotlin
gameArea.setBlockAt(
    player.position,
    Block.create(Tile.newBuilder()
        .withCharacter(player.character)
        .withForegroundColor(player.color)
        .build())
)
```

## The `Engine` class

Let's start this off by saying that a lot of the stuff which is done in the tutorial's `Engine` class is
to some extent already implemented a bit differently in Zircon. For now, I will implement most of the
stuff like the tutorial does, and change it in the very near future.

Again, it was pretty much just copying and pasting stuff around.

```kotlin
class Engine(private val entities: List<Entity>, private val player: Entity) {

    fun handleEvents(event: KeyboardEvent) {
        when (val action = handleKeyboardEvent(event)) {
            is EscapeAction -> exitProcess(0)
            is MovementAction -> {
                player.position = player.position.withRelativeX(action.dx).withRelativeY(action.dy)
            }
        }
    }

    fun render(gameArea: GameArea<Tile, Block<Tile>>) {
        
        for (x in 0..gameArea.actualSize.xLength) {
            for (y in 0..gameArea.actualSize.yLength) {
                gameArea.setBlockAt(Position3D.create(x, y, 0), Block.create(Tile.defaultTile()))
            }
        }

        for (entity in entities) {
            gameArea.setBlockAt(
                entity.position,
                Block.create(
                    Tile.newBuilder()
                        .withCharacter(entity.character)
                        .withForegroundColor(entity.color)
                        .build()
                )
            )
        }
    }
}
```

At the very least, the `main.kt` file looks a lot cleaner now. Which is a big advantage by itself.

## Adding a (static) game map

Good news is, the `gameArea` is already what the game map is in the tutorial. 
Bad news is - it's not exactly how I would need it. In the tutorial, the game map only represents
the structure of the dungeon itself, and then there is a separat console object to render it's contents to.

The `gameArea` is pretty much both. A three-dimensional representation of the game world, which also
directly renders it's contents due to being wrapped in a `GameComponent`, but I need to redraw each
of the tiles on every update, or at least when an entity changed it's place. Basically, the same
problem as before, but my solution didn't really solve the problem at all.

The two possibilities are to either have a separate map where I have the structure of the base map
defined, or to modify the `GameArea` a bit. I think I'll just do the latter. Which also
saves me from keeping this code any longer:

```kotlin
for (x in 0..gameArea.actualSize.xLength) {
    for (y in 0..gameArea.actualSize.yLength) {
        gameArea.setBlockAt(Position3D.create(x, y, 0), Block.create(Tile.defaultTile()))
    }
}
```

To solve the problem here, each position on the game world must store its original `Tile`. I don't
think building a new implementation of `GameArea` will do the trick, because, in its very basics, it's
just a collection of blocks anyways. A `Block` is, as far as I understood it, a collection of 
information about a single position in the three-dimensional world. 

The philosophical question now is - are walls and floors entities, too? Or are they just tiles? 
Going the _everything is an entity_ way would bring me a bit further away from the tutorial, but 
opens a lot of nice other opportunities, and might even make thing less complex in the future, 
especially when I think of collision detection and FOV stuff.

So I think I'll go with the _everything is an entity_ approach.

So, I'll start with a new implementation for `Block`. Naming stuff isn't really my strength, so I
guess I'll just call it `WorldBlock`. The only thing I already know is that I will need a list of 
entities there.

This leads me to this class for a beginning

```kotlin
class WorldBlock(private val entities: MutableList<Entity> = mutableListOf()) :
    BaseBlock<Tile>(Tile.empty(), persistentMapOf()) {

   override fun createCopy(): Block<Tile> {
       return WorldBlock(emptyTile)
   }
}
```
At this point I don't know if it's necessary, but the default block implementation which is provided by
Zircon, `DefaultBlock`, has a `createCopy` method implementation, too, so I added a quick one here.

With that code, I can have basically as many entities as I want to on a specific position. So I need a way
to determine which tile I want to display. Basically, I will go for _actor -> item -> furniture -> terrain_, where
item is everything which the player can put in the inventory, and furniture everything the player can't 
put in the inventory (including corpses). 

I need to update the `Entity` class for that purpose.

```kotlin
class Entity(
    var position: Position3D,
    val type: EntityType,
    val tile: Tile
)

enum class EntityType {
    ACTOR, ITEM, FURNITURE, TERRAIN
}
``` 

I did not only add the `EntityType`, but instead of `character` and `tileColor`, an entity now also
holds a `Tile`. Of course, this means changing every entity which is available in the code. But that
is not too much of a problem, since there are only 2 of them so far.

Now, some more changes in the WorldBlock. First of all, I want the `emptyTile` to always return the 
tile of the `TERRAIN` entity on this block.

```kotlin
    override val emptyTile: Tile
        get() =
            when {
                entities.any { it.type == EntityType.TERRAIN } -> entities.first { it.type == EntityType.TERRAIN }.tile
                else -> Tile.empty()
            }
```

I swear, I do love Kotlin more with every single line I write.

Next thing is - I need a way to add and remove entities from the Block. The `addEntities` method does
also sort the list of entities by the entity type, which means all I have to do is to display
the first (or last) element.

```kotlin
fun addEntity(entity: Entity) {
    entities.add(entity)
    entities.sortBy { it.type }
}

fun removeEntity(entity: Entity) {
    entities.remove(entity)
}
```

I do not check for an already existing other `TERRAIN` entity here, even if it would make sense to some
extent. But the terrain is pretty static for now, so I don't really see that it's necessary to add at
the moment.

And, to finalize this (for now at least), I will override the `content` member, too. As far as I
understood, this one is responsible for which `Tile` is getting displayed.

```kotlin
override var content: Tile
    get() = entities.first().tile
    set(value) {}
```

With some small changes in both the `main.kt` and the `Engine` class, the code compiles again.

Aaaaand... the screen goes black.
Ok then, next try. I'll override the `tiles[CONTENT]` element. If there's nothing set in the other
possible fields of `tiles`, the `emptyTile` will be used instead, so I think I only need to
override this one.

```kotlin
override var tiles: PersistentMap<BlockTileType, Tile>
    get() = persistentMapOf(
        Pair(
            BlockTileType.CONTENT, when {
                entities.isEmpty() -> emptyTile
                else -> entities.first().tile
            }
        )
    )
    set(value) {}
```

It compiles, and shows the characters again. The line `entities.isEmpty() -> emptyTile` is needed
in case there is no entity set at all. Otherwise an exception might happen, and the screen stays black.

Only one problem is remaining. I changed the second entity, the `npc` to the type `TERRAIN`, and set
a background color 

```kotlin
val npc = Entity(
    Position3D.create(screenSize.width / 3, screenSize.height / 3, 0),
    EntityType.TERRAIN,
    Tile.newBuilder()
        .withCharacter('@')
        .withForegroundColor(TileColor.create(0, 200, 200))
        .withBackgroundColor(TileColor.create(70, 0, 0))
        .build()
)
```

But I already figured it out - instead of overriding `tiles[CONTENT]`, I need to override `tiles[TOP]`.

And I still have the problem that moving around the player leaves a trail again. Temporarily, I'll
just fix this by remvoing all entities before I do the input handling.

## Extending the entities

Because I chose to have an `Entity` for _anything_ on the map, stuff like being a blocking or transparent
tile is, of course, also defined in the `Entity` class, instead of directly on the `Tile` like in the 
libtcod tutorial.

So, an `Entity` can be `walkable` and `transparent`. To be fully honest here, just adding these two
parameters as booleans isn't how I would usually approach this, because there are a lot of 
different options to consider. For example, the player can't exist on the same tile as another `ACTOR`, but
it's entirely possible to share a tile with some `ITEM`s, and `FURNITURE` might also be on the same tile,
but a, let's say, barrel would block player movement, but some items might be ontop of it. Same with
`TERRAIN` - a floor pretty much allows everything to be on the same tile, a wall usually doesn't share
its place with anything else.

For now, I will just continue like the tutorial suggests, but I know this is something I will have
to change soon.
```kotlin
class Entity(
    var position: Position3D,
    val type: EntityType,
    val tile: Tile,
    val walkable: Boolean = true,
    val transparent: Boolean = true
)
```
As you can see, I completely skipped the `dark` part, for now. I will add this a bit differently later,
when the FOV stuff is getting implemented.
