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
