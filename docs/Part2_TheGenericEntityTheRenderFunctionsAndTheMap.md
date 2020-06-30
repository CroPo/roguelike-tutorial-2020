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

