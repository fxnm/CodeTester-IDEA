package de.fxnm.config.settings.password_safe

class Key(private val keyValue: String) {
    override fun toString(): String {
        return String.format("Key [%s]", keyValue)
    }
}
