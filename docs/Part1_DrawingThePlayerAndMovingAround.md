# Part 1 - Drawing the ‘@’ symbol and moving it around

- [Reddit Post on /r/roguelikedev](https://old.reddit.com/r/roguelikedev/comments/ha1zty/so_it_begins_roguelikedev_does_the_complete/)
- [Original tutorial](http://rogueliketutorials.com/tutorials/tcod/part-1/)

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
