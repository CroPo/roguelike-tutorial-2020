package com.cropo.action

import com.cropo.engine.Engine

interface Action {
    fun perform(engine: Engine, entity: Entity)
}