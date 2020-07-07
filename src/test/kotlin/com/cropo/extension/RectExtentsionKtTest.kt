package com.cropo.extension

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class RectExtentsionKtTest {

    @ParameterizedTest
    @MethodSource("positions")
    fun testRectCreate(fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val from = Position.create(fromX, fromY)
        val to = Position.create(toX, toY)

        val rect = Rect.create(from, to)

        // The rectangle must contain both positions
        assertTrue(rect.containsPosition(from))
        assertTrue(rect.containsPosition(to))
    }

    companion object {
        @JvmStatic
        fun positions() = listOf(
            Arguments.of(1, 1, 9, 9),
            Arguments.of(9, 3, 2, 7),
            Arguments.of(9, 9, 1, 1),
            Arguments.of(3, 9, 7, 2)
        )
    }

}