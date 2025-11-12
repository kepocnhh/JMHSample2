package org.kepocnhh.jmh

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
internal open class FooBenchmark {
    @Benchmark
    @Fork(value = 1, warmups = 0)
    @Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS, batchSize = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    fun addBenchmark(hole: Blackhole, state: FooState) {
        check(state.foo.size() == 0)
        state.foo.add()
        check(state.foo.size() == 1)
        hole.consume(state.foo)
    }
}
