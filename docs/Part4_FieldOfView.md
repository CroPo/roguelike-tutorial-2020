# Part 3 - Field of view

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/hif9df/roguelikedev_does_the_complete_roguelike_tutorial/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/v2/part-4/)

## Making tiles explorable

Until now, a `Tile` doesn't have any state - it's pretty much just a data class for a character and some colors
in my context, and that's how I want to keep it.  

So the place to add the _explored_ state, and of course, a second optional `Tile` is the `Entity` class.
```kotlin
class Entity(
    var position: Position3D,
    val type: EntityType,
    val tile: Tile,
    val tileHidden: Tile? = null,
    val isWalkable: Boolean = true,
    val isTransparent: Boolean = true,
    var isExplored: Boolean = false
)
```
The `Entity` class gets more and more cluttered here with stuff not every entity would need, 
which I will take care of in the next part of the tutorial, when there will be more than just terrain entities.

Mind this line: `val tileHidden: Tile? = null,` - the `?` next to a type means that this variable is _nullable_ and
will need some special treatment whenever it's used.

I also prefixed all booleans with an `is`.

## Creating a FOV

For the field of vision, I will just use a set of booleans, where `true` means visible and `false` means not visible,
there is no need for a _partially visible_ state at the edge of the FOV at the moment.

I could either create an fov overlay for the whole map, or a smaller one which is positioned relative to the player. I 
could even make it smaller by generating just a list of positions which are visible. This means - once again, I will
add something to the `Entity` class. It seems fitting that the FOV is part of at least the player entity.

```kotlin
val fieldOfVision: MutableList<Position3D>? = null
```

Again, this val is nullable. I also need to pass an initial value here, since `fieldOfVision` is a val, not a var,
which means is technically `final`.

```kotlin
    val player = Entity(
        position = Position3D.defaultPosition(),
        type = EntityType.ACTOR,
        tile = Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(TileColor.transparent())
            .withForegroundColor(TileColor.defaultForegroundColor())
            .build(),
        fieldOfVision = mutableListOf()
    )
```

This is how the player entity creation looks now.

To update the FOV, I added a `Action` class - `UpdateFovAction`, which is still empty right now. It is triggered after
every action the player takes.

## Calculating the FOV

I did a bit of digging around in the zircon library, and I found their implementation of a few Bresenham shape
algorithms, which is just what I need for the calculation.

First, I will need to create a circle containing all possible visible positions. Right now, the vision range of
the player is a diameter of 11 units.
```kotlin
val fovCircle = EllipseFactory.buildEllipse(
    EllipseParameters(entity.position.to2DPosition(), Size.create(11,11)))
```

And for the actual calculation, I will cast a `Line` from the center to each available position inside the FOV circle:

```kotlin
val visiblePositions: MutableList<Position3D> = mutableListOf()
fovCircle.positions.forEach { fovPosition ->
    LineFactory.buildLine(center, fovPosition).positions
        .filterNot { visiblePositions.contains(it.to3DPosition(0)) }
        .takeWhile { linePosition ->
            !engine.entities.filter { it.position.to2DPosition() == linePosition }
                .any { !it.isTransparent }
        }
        .forEach { visiblePositions.add(it.to3DPosition(0)) }
}
```

This should bascially work I think.