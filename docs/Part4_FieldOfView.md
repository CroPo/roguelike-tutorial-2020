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

