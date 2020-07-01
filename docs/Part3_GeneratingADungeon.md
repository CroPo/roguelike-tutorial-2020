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
