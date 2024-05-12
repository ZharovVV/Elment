package com.github.elment.sample.ui.case1

import com.github.elment.core.config.ThrottlingConfig
import com.github.elment.core.config.TimeSelector

class Case1ThrottlingConfig : ThrottlingConfig<Case1Event> {
    override val debounceTimeout: TimeSelector<Case1Event> = { case1Event: Case1Event ->
        when (case1Event) {
            Case1Event.Decrease -> 3000L
            Case1Event.Increase -> 1000L
        }
    }
    override val throttlingWindow: TimeSelector<Case1Event> = { case1Event: Case1Event ->
        when (case1Event) {
            Case1Event.Decrease -> 3000
            Case1Event.Increase -> 1000
        }
    }
}