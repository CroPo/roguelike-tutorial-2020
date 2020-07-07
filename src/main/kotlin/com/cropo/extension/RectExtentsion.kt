package com.cropo.extension

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.internal.data.DefaultRect

/**
 * Creates a new [Rect] including both given [Position]s as opposite corners
 */
fun Rect.Companion.create(from: Position, to: Position): Rect {

    val (left, right) = if (from.x > to.x) {
        Pair(to.x, from.x + 1)
    } else {
        Pair(from.x, to.x + 1)
    }

    val (top, bottom) = if (from.y > to.y) {
        Pair(to.y, from.y + 1)
    } else {
        Pair(from.y, to.y + 1)
    }

    val topLeft = Position.create(left, top)
    val bottomRight = Position.create(right, bottom)

    return DefaultRect(topLeft, bottomRight.minus(topLeft).toSize())
}