package de.fxnm.web.components.submission.success

import java.util.Arrays
import java.util.LinkedList
import java.util.stream.Collectors

class CheckFileData(val name: String, private val content: String) {

    val contentArray: Array<String>
        get() {
            val output: MutableList<String> = LinkedList()
            output.addAll(Arrays.stream(content.split("\n".toRegex()).toTypedArray())
                    .map { s: String -> s.replace(" ", "␣") }
                    .collect(Collectors.toList()))
            return output.toTypedArray()
        }

    fun isEmpty(): Boolean {
        return content == ""
    }
}
