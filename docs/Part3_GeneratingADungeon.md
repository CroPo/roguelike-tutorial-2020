# Part 3 - Generating a dungeon

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/he3lfo/roguelikedev_does_the_complete_roguelike_tutorial/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/v2/part-3/)

I did a bit of cleanup between the end of part two and this part, including adding eight-directional movement
with the numpad.

## Filling the map with tiles

In order to do the dungeon generation like the tutorial suggests, the map needs to be filled with walls, where
the floors will be _carved out_.

That means in my case generating an `Entity` for each available position. For this, I could build an own 
implementation for `GameArea`, where the dungeon is being generated in the constructor. There is just one problem then - 
I would need a way to return the created entities back to the `Engine`, so they will get packed into the
entities list. It would be pretty much possible to just pass the list to the constructor, and that's it.

But there are a few more things to consider. First of all, `GameArea` supports three dimensions. This means for
one that the same object could theoretically hold multiple levels of the dungeon at once, but also that I would have
an immense number of absolutely unused entities which just gets higher the further the player descends down the dungeon.
The obvious solution here would be to simply delete all the wall-entities which the player won't ever be able to see,
because they're behind another wall. But for now, I don't even know if I do have that problem, and as long as no
problem appear there is no need for premature optimization.

With all of that in mind, I created this first draft here
```kotlin
class World(
    actualSize: Size3D,
    visibleSize: Size3D,
    entities: MutableList<Entity>,
    entityBlueprint: (Position3D) -> Entity
) : BaseGameArea<Tile, WorldBlock>(actualSize, visibleSize) {
    init {
        actualSize.fetchPositions().forEach { position ->
            val entity = entityBlueprint(position)
            val block = WorldBlock()
            block.addEntity(entity)
            entities.add(entity)
            setBlockAt(position, block)
        }
    }
}
```
It seems to work well so far, so I'll continue with the actual dungeon generation.

## Generating a simple room

The simplest way of implementing a dungeon generator I can think of, which still gets _some_
interesting results, is pretty much the one which the tutorial describes - a bunch
of interconnected rectangles. 

I will first implement that, and, if there is a bit of time (which I doubt a bit, since I'm 
already more than a week late with the tutorial), I'll also implement a cave generator with cellular
automata.

There are a few questions I need to ask myself first, before beginning to build the dungeon generator.
First of all - _where_ should it be located? I could pass it directly to the `World`. Which would
make sense in some way - some information I need is part of `World` already (the size in this case).

I could also make it in a way that I actually get a `World` object as return value of the generator.
The caveat here is that `GameComponent` doesn't support replacing the wrapped `GameArea`, at least
not out of the box. I personally use the `GameArea` (which `World` is an implementation of) as 
a canvas to paint on, and I think it's basically also meant like that.

I could also handle both the `World` and the dungeon generator without any form of direct coupling.
The dungeon generator practically just needs to return a collection of entities, which then will
be put to their respective positions in the `World`.

And the last one is probably the way I should do it. No direct coupling to `World`, at all. The
only interaction is when put the result of my dungeon generator in some way to the `World`. 
The dungeon generator doesn't need to know how `World` handles the information, and `World`
doesn't care where the information is from, as long as it's a `List<Entity>`. The thing is,
I don't even need to pass the created entities directly to `World`, I just need to make sure
`World` will update it's internal state - be it a replacement of the current z level, or addition
of a new one (this is, of course, depending on how much code I need to write to be able to 
do either one of those) - after the entity list has been updated by the dungeon generator.

With that decision made, I can start with the dungeon generator, finally.

Right now, I can't really think of any specific parameter the dungeon generator could need. It 
shouldn't update the entity list directly, but just return a `List<Entity>`, so this parameter
isn't needed, either. The only thing the dungeon generator really _needs_ to know are the
dimensions of the dungeon.

The first draft of the interface is kept pretty simple

```kotlin
class DungeonGenerator {

    val size: Size

    /**
     * Generate a list of entities which represent a single level of the dungeon.
     */
    fun generateLevel(): List<Entity>
}
```

The first implementation, which creates just one rectangular room, looks like this

```kotlin
override fun generateLevel(): List<Entity> {
    val offset = Position.create(35, 20);
    val size = Size.create(11, 7)
    val innerSize = size.minus(Size.create(2, 2))

    val room = MutableList(size.width * size.height) { i ->
        val x = i / size.height
        val y = i % size.height

        EntityBlueprint.wallEntity(Position3D.create(x + offset.x, y + offset.y, 0))
    }

    for (x in 0 until innerSize.width) {
So I'm going to change all of that to 
        val iOffset = x * size.height + size.height + 1

        for (y in 0 until innerSize.height) {
            val i = iOffset + y
            room[i] = EntityBlueprint.floorEntity(Position3D.create(x + offset.x + 1, y + offset.y + 1, 0))
        }
    }

    return room
}
```
It's again back to _fill everything up with walls and carve out the room_, which works well in general. 
But the one thing I don't like too much about this piece of code is that Entities are generated
right from the start, and, depending on the underlying algorithm, will be deleted and recreated 
again and again - and the same happens when connecting multiple rooms with corridors.

## A proper dungeon generator

What do I need to build a proper level generator? Well, first of all, an idea on how to do it. This is now my third or
even fourth try to come up with something not absolutely stupid.

The generator should only come up with a layout for a level, not a complete set of entities. Entities are something,
well, more or less final. The layout should only contain a quick view of where is a wall, where is a floor, and so
on, which are easy to modify. Entities are also a bit too dynamic in their structure - for some checks and
compares during level generation I need to be sure about what I have.

By this, a list of booleans would basically be fine - where `true` is a wall and `false` is a floor. 

But I might want to add other features at that stage. Like the stairs to other dungeon levels, maybe doors, traps,
secret doors, and so on. So I don't want to restrict myself to just a boolean list. So I think I will go on by creating
a `enum class` for all elements I want to be able to generate.

```kotlin
enum class LayoutElement {
    FLOOR, WALL
}
```

Right now, only two elements are supported - but this will change rapidly.

The layout itself first of all needs a list of elements, and its bounds. It also should be possible to _merge_ other
layouts into another layout - this is needed when I generate all the smaller rooms and then put them into the bigger
dungeon map, for example. It could also be used to load all kind of prefab rooms, and so on.

```kotlin
class Layout(
    private val bounds: Rect
) {
    private val internalTerrain: MutableMap<Position, LayoutElement> = HashMap()

    val terrain : Map<Position, LayoutElement>
    get() = internalTerrain

    init {
        bounds.fetchPositions().forEach {
            internalTerrain[it] = LayoutElement.WALL
        }
    }
}
```

On creation of a `Layout` object, the map containing will be initialized with all coordinates in the given
bounds, each set to a `LayoutElement.WALL`. The coordinates in a `Rect` are all automatically calculated with
the offset in mind, so to merge two layouts, I just have to override one map with the other.

To access the terrain from outside I made a special getter which only allows read access. Modification should
only be available through the API of `Layout`.

With the `DungeonGenerator` modified, I can now generate a full level of walls, again!

```kotlin
class DungeonGenerator(private val mapSize: Size) {
    
    private var levelLayout = Layout(Rect.create(Position.topLeftCorner(), mapSize))
    
    fun generateLevel(): List<Entity> {
        val room = mutableListOf<Entity>()

        levelLayout.terrain.forEach { (position: Position, element: LayoutElement) ->
            room.add(
                when (element) {
                    FLOOR -> EntityBlueprint.floorEntity(position.to3DPosition(0))
                    WALL -> EntityBlueprint.wallEntity(position.to3DPosition(0))
                }
            )

        }
        return room
    }
}
```

Now that the basic work for the dungeon generation is done, I can start implementing a way to generate multiple
rooms - be it simple rectangular ones as the tutorial suggest, or more complex ones using cellular automata 
for example. And because at least the latter doesn't really generate a single room all the time, but more like
a connected system of rooms already, I will have to find a better name than `Room`.

_Section_ and _area_ come to my mind - and I think I'll go with _section_ here (I really don't like naming stuff).
I renamed the `Layout` class to `Section` - because it already contains all the data I need, and I don't really think
it's necessary to wrap it in another class just for the sake of it.

 