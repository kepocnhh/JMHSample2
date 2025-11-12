package org.kepocnhh.jmh

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
internal open class FooBenchmark {
    private var foo: Foo? = null

    @Setup(Level.Trial)
    fun eachTrial() {
        foo = Foo()
    }

    @Benchmark
    fun addBenchmark(hole: Blackhole) {
        val foo = foo ?: error("No foo!")
        check(foo.size() == 0)
        foo.add()
        check(foo.size() == 1)
        hole.consume(foo)
    }
}
