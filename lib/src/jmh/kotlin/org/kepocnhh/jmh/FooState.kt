package org.kepocnhh.jmh

import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Thread)
internal open class FooState {
    val foo = Foo()
}
