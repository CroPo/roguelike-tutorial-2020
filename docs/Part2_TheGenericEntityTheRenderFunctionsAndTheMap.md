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

I will probably expand this a bit further
