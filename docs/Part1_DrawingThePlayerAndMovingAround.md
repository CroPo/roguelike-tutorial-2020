# Part 1 - Drawing the ‘@’ symbol and moving it around

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/ha1zty/so_it_begins_roguelikedev_does_the_complete/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/v2/part-1/)

## Opening a window

So, let's get started with this. The first thing I want to create is a grid which I can draw tiles
on. 
 
```kotlin
val grid: TileGrid = SwingApplications.startTileGrid()
``` 

This also creates an instance of a swing-backed `Application`, which automatically takes care of
the drawing process. Yes, this line alone draws a window on the screen.

The basic configuration, both for the window and the application, are set with the `AppConfig` class,
which, conveniently, provides a fluent builder.

So, with this I set a title, and the window size to 80 x 50 characters:

```kotlin
val grid: TileGrid = SwingApplications.startTileGrid(
    AppConfig.newBuilder()
        .withTitle("/r/roguelikedev tutorial 2020")
        .withSize(80, 50)
        .build()
)
```

The one thing left to do is configuring the tileset. Zircon also comes bundled with a lot of tilesets already, so I'll just pick one which I know
I like. This also takes care of setting up the tile size correctly.

```kotlin
val grid: TileGrid = SwingApplications.startTileGrid(
        AppConfig.newBuilder()
            .withTitle("/r/roguelikedev tutorial 2020")
            .withSize(80, 50)
            .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
            .build()
    )
```

When I run the code, an empty window is getting displayed, with the set title and size.

### Drawing the player

Traditionally, the player is represented by an `@`, so I'll just try to draw that into the grid.
I just tried around, and, apparently, this does the job:

```kotlin

    grid.draw(
        Tile.createCharacterTile('@', StyleSet.defaultStyle()),
        Position.create(40, 25)
    )

```

The default style is white on black, and this fits just fine for now.

### Utilizing Zircon

While the player character does show up on the exact location where I want him to be, it's not really the correct way
to display it.

Zircon uses a system of GUI Components to display stuff on the screen. One of those components is the `GameComponent`,
which is (basically) responsible for rendering the world in a given viewport.

It takes a `GameArea` - a 3D representation of all available tiles, so, more or less, a 3D map of the game, which has
a defined size, and a visible size.

With this, I create a game area with the same size as the window, and wrap it into a component, which itself is attached
to the screen.

```kotlin
val screen = ScreenBuilder.createScreenFor(grid)

val gameArea = GameAreaBuilder.newBuilder<Tile, Block<Tile>>()
    .withVisibleSize(Size3D.create(80, 50, 1))
    .withActualSize(Size3D.create(80, 50, 1))
    .build()

screen.addComponent(
    GameComponentBuilder.newBuilder<Tile, Block<Tile>>()
        .withGameArea(gameArea)
        .build()
)
```

And with this, the player character is displayed at the defined position

```kotlin
gameArea.setBlockAt(
    Position3D.create(40,25,0),
    Block.create(Tile.createCharacterTile('@', StyleSet.defaultStyle()))
)
```

### Moving the player around

The next thing to do is some even handling, to move the player around.
First of all, instead of hardcoding the player position, I will use a `Position3D` to store the current position.
Currently, there are only the x and y coordinate needed, but Zircon works only three-dimensional, so the z coordinate
will always be 0, at least for now.

```kotlin
var playerPosition = Position3D.create(40,25,0)
```

I'm using `var` instead of `val`, because this variable will change.

Now to some event handling. I can simply attach an event handler directly to the `screen`

```kotlin
screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->

    when (event.code) {
        KeyCode.UP -> playerPosition = playerPosition.withRelativeY(-1)
        KeyCode.DOWN -> playerPosition = playerPosition.withRelativeY(1)
        KeyCode.LEFT -> playerPosition = playerPosition.withRelativeX(-1)
        KeyCode.RIGHT -> playerPosition = playerPosition.withRelativeX(1)
        KeyCode.ESCAPE -> exitProcess(0)
        else -> {
        }
    }

    gameArea.setBlockAt(
        playerPosition,
        Block.create(Tile.createCharacterTile('@', StyleSet.defaultStyle()))
    )

    Pass
}
```

With this code, I can move the `@` around with the arrow keys. Since there is no `clear` or `overrideAll` method 
for the `gameArea`, there is a trail of `@` characters at the moment. 

There are basically a few ways to handle this. First, I could override the whole `gameArea` every time something moves.
Secondly, I could just replace the former position with an empty tile. Or I could create a separate layer to draw all
the game objects on, which I can clear before every update.

The first one would pretty much be the easiest one, and is basically the same way the libtcod tutorial handles it.

Resetting the current position after moving to another needs some extended effort: I need to save which tile was on
the occupied position before. This may work with one movable tile, but it's prone to errors once more than one moveable
entity is introduced. 

The layer thing might be the cleanest at first glance, but the layer needs to be put on the screen, and not on a component.
Also, if there is a need for maps with bigger than screen size, rearranging the layer might become a problem because
there is no direct interaction between the layer and the game component.

So, I just go with the _redraw everything_ option. Which shouldn't really be a problem on a somewhat modern processor.

That means, before I move the player I need to do this:

```kotlin
for (x in 0..80) {
    for(y in 0..50) {
        gameArea.setBlockAt(Position3D.create(x,y,0), Block.create(Tile.defaultTile()))
    }
}
```

With that I now have a, well, big pile of messy code, at least for now. But it's a big pile of messy code which works,
and this is fine for now.