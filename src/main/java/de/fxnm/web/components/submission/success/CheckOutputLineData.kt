package de.fxnm.web.components.submission.success

class CheckOutputLineData(val type: String, private val content: String) {

    fun getContent(): String {
        return if (type == "ERROR" || type == "OTHER") {
            content
        } else {
            content.replace(" ", "␣")
        }
    }
}
