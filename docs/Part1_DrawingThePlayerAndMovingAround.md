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

The default style is white on black as it seems, which is fine for now, but I might change it later.
