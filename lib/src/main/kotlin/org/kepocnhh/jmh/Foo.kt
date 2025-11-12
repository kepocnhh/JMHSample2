package org.kepocnhh.jmh

class Foo {
    private val values = mutableListOf<String>()

    fun add() {
        values += values.size.toString()
    }

    fun size(): Int {
        return values.size
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Foo -> values == other.values
            else -> false
        }
    }

    override fun hashCode(): Int {
        return values.hashCode()
    }
}
