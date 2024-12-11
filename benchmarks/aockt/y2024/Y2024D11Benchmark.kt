package aockt.y2024

import kotlinx.benchmark.*

@State(Scope.Benchmark)
class Y2024D11Benchmark {
    val benchInput = "4 91757 1103 8910902 1 827 0 916290 21"

    @Benchmark
    fun benchPartOne(): Int {
        return Y2024D11.partOne(benchInput)
    }

    @Benchmark
    fun benchPartTwo(): Long {
        return Y2024D11.partTwo(benchInput)
    }

}
