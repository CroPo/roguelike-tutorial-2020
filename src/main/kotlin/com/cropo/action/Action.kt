package com.cropo.action

import com.cropo.engine.Engine
import com.cropo.entity.Entity

interface Action {
    fun perform(engine: Engine, entity: Entity)
}