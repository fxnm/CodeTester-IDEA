package de.fxnm.config.settings.global

import java.util.LinkedList

class GlobalSettingsData {

    val codeTesterBaseURL: MutableList<String> = LinkedList(listOf("https://codetester.ialistannen.de"))

    fun reset() {
        codeTesterBaseURL.clear()
    }
}
