package de.fxnm.config.settings.global

import java.util.*

class GlobalSettingsData {

    val codeTesterBaseURL: MutableList<String> = LinkedList(listOf("https://codetester.ialistannen.de"))

    fun reset() {
        codeTesterBaseURL.clear()
    }
}
