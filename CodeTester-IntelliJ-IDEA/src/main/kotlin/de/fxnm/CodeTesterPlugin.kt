package de.fxnm

import java.lang.IllegalAccessException

class CodeTesterPlugin private constructor() {

    companion object {
        const val PLUGIN_ID = "CodeTester-IDEA"
    }

    init {
        throw IllegalAccessException()
    }
}
