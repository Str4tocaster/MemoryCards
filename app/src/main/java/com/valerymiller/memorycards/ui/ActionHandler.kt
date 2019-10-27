package com.valerymiller.memorycards.ui

import android.os.Handler
import android.os.Message

class ActionHandler(val action: () -> Unit): Handler() {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        action()
    }
}